#!/usr/bin/env python3
#----------------------------------------------------------------------------
# Copyright (c) 2018 FIRST. All Rights Reserved.
# Open Source Software - may be modified and shared by FRC teams. The code
# must be accompanied by the FIRST BSD license file in the root directory of
# the project.
#----------------------------------------------------------------------------

import json
import time
import sys

import numpy as np
import cv2 as cv
from matplotlib import pyplot as plt

from cscore import CameraServer, VideoSource, UsbCamera, MjpegServer
from networktables import NetworkTablesInstance

#   JSON format:
#   {
#       "team": <team number>,
#       "ntmode": <"client" or "server", "client" if unspecified>
#       "cameras": [
#           {
#               "name": <camera name>
#               "path": <path, e.g. "/dev/video0">
#               "pixel format": <"MJPEG", "YUYV", etc>   // optional
#               "width": <video mode width>              // optional
#               "height": <video mode height>            // optional
#               "fps": <video mode fps>                  // optional
#               "brightness": <percentage brightness>    // optional
#               "white balance": <"auto", "hold", value> // optional
#               "exposure": <"auto", "hold", value>      // optional
#               "properties": [                          // optional
#                   {
#                       "name": <property name>
#                       "value": <property value>
#                   }
#               ],
#               "stream": {                              // optional
#                   "properties": [
#                       {
#                           "name": <stream property name>
#                           "value": <stream property value>
#                       }
#                   ]
#               }
#           }
#       ]
#   }

configFile = "/boot/frc.json"

class CameraConfig: pass

team = 6955
server = False
cameraConfigs = []

"""Report parse error."""
def parseError(str):
    print("config error in '" + configFile + "': " + str, file=sys.stderr)

"""Read single camera configuration."""
def readCameraConfig(config):
    cam = CameraConfig()

    # name
    try:
        cam.name = config["name"]
    except KeyError:
        parseError("could not read camera name")
        return False

    # path
    try:
        cam.path = config["path"]
    except KeyError:
        parseError("camera '{}': could not read path".format(cam.name))
        return False

    # stream properties
    cam.streamConfig = config.get("stream")

    cam.config = config

    cameraConfigs.append(cam)
    return True

"""Read configuration file."""
def readConfig():
    global team
    global server

    # parse file
    try:
        with open(configFile, "rt") as f:
            j = json.load(f)
    except OSError as err:
        print("could not open '{}': {}".format(configFile, err), file=sys.stderr)
        return False

    # top level must be an object
    if not isinstance(j, dict):
        parseError("must be JSON object")
        return False

    # team number
    try:
        team = j["6955"]
    except KeyError:
        parseError("could not read team number")
        return False

    # ntmode (optional)
    if "ntmode" in j:
        str = j["ntmode"]
        if str.lower() == "client":
            server = False
        elif str.lower() == "server":
            server = True
        else:
            parseError("could not understand ntmode value '{}'".format(str))

    # cameras
    try:
        cameras = j["cameras"]
    except KeyError:
        parseError("could not read cameras")
        return False
    for camera in cameras:
        if not readCameraConfig(camera):
            return False

    return True

"""Start running the camera."""
def startCamera(config):
    print("Starting camera '{}' on {}".format(config.name, config.path))
    inst = CameraServer.getInstance()
    camera = UsbCamera(config.name, config.path)
    server = inst.startAutomaticCapture(camera=camera, return_server=True)

    camera.setConfigJson(json.dumps(config.config))
    camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen)

    if config.streamConfig is not None:
        server.setConfigJson(json.dumps(config.streamConfig))

    return camera

if __name__ == "__main__":
    if len(sys.argv) >= 2:
        configFile = sys.argv[1]

    # read configuration
    if not readConfig():
        sys.exit(1)

    # start NetworkTables
    ntinst = NetworkTablesInstance.getDefault()
    if server:
        print("Setting up NetworkTables server")
        ntinst.startServer()
    else:
        print("Setting up NetworkTables client for team {}".format(team))
        ntinst.startClientTeam(team)

    # start cameras
    cameras = []
    for cameraConfig in cameraConfigs:
        cameras.append(startCamera(cameraConfig))
    camera = cameras[0]
    camera.set(cv2.CV_CAP_PROP_FRAME_WIDTH, 320)
    camera.set(cv2.CV_CAP_PROP_FRAME_HEIGHT, 240)

    # User code
    sd = ntinst.getTable("vision")

    while True:
        # Capture frame
        _, frame = camera.read()
        # Apply gaussian blur with 5x5 kernel
        blur = cv2.GaussianBlur(frame, (5, 5), 0)
        # Conver to HSV and gray
        hsv = cv2.cvtColor(blur, cv2.COLOR_BGR2HSV)
        Thold = cv2.cvtColor(blur, cv2.COLOR_BGR2GRAY)
        # Segmentation
        lower_green = np.array([49, 0, 100], dtype=np.uint8)
        upper_green = np.array([59, 150, 255], dtype=np.uint8)
        mask = cv2.inRange(hsv, lower_green, upper_green)
        # Threshold operation
        ret, thr = cv2.threshold(mask, 130, 255, cv2.THRESH_OTSU)
        #Contours
        contours, hierarchy = cv2.findContours(thr, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        contours = sorted(contours, key=cv2.contourArea, reverse=True)
        cv2.drawContours(blur, contours, -1, (0, 255, 0), 0)
        perimeters = [cv2.arcLength(contours[i], True) for i in range(len(contours))]
        # Morphology to eliminate islands
        res = cv2.bitwise_and(blur, blur, mask=mask)
        kernel = np.ones((11, 11), np.float32) / 225
        opening = cv2.morphologyEx(blur, cv2.MORPH_OPEN, kernel)
        erosion = cv2.erode(opening, kernel ,iterations=1)
        dilation = cv2.dilate(erosion, kernel, iterations=1)
        # Calculate moments
        moments = cv2.moments(mask)
        area = moments['m00']
        if area > 200:
            x = int(moments['m10'] / moments['m00'])
            y = int(moments['m01'] / moments['m00'])
            print ("x =", x)
            print ("y =", y)
            cv2.rectangle(dilation, (x, y), (x + 2, y + 2), (0, 0, 255), 2)
        else:
            x, y = -1, -1
        cv2.imshow('Camara', frame)
        cv2.imshow('Thr',thr)
        cv2.imshow('Final',dilation)
        sd.putNumber("error", x)
        k = cv2.waitKey(20)
