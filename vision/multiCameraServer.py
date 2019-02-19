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
import cv2
# from matplotlib import pyplot as plt

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
        team = j["team"]
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
    # for cameraConfig in cameraConfigs:
    #     cameras.append(startCamera(cameraConfig))
    # camTargets = cameras[0]
    camTargets = cv2.VideoCapture(0)
    camTargets.set(cv2.CV_CAP_PROP_FRAME_WIDTH, 320)
    camTargets.set(cv2.CV_CAP_PROP_FRAME_HEIGHT, 240)
    # camCargo = cameras[1]
    # camCargo.set(cv2.CV_CAP_PROP_FRAME_WIDTH, 320)
    # camCargo.set(cv2.CV_CAP_PROP_FRAME_HEIGHT, 240)
    # Capture frame Cargo
    # camCargo.set(3,320.0)
    # camCargo.set(4,240.0)

    # mpjpegServer = MjpegServer("CameraBack", "", 1181)

    # User code
    sd = ntinst.getTable("vision")
    while(True):
        # Capture frame Targets
        _, frame = camTargets.read()
        # Apply gaussian blurT with 5x5 kernel to Targets
        blurT = cv2.GaussianBlur(frame,(5,5),0)
        # Convert to HSV and gray to Targets
        hsvT = cv2.cvtColor(blurT, cv2.COLOR_BGR2HSV)
        Thold = cv2.cvtColor(blurT, cv2.COLOR_BGR2GRAY)
        # SegmentationTargets
        lower_green = np.array([30, 0, 100], dtype=np.uint8)
        upper_green = np.array([59, 150, 255], dtype=np.uint8)
        maskT = cv2.inRange(hsvT,lower_green, upper_green)
        # Threshold operation Targets
        ret, thrT = cv2.threshold(maskT, 130, 255, cv2.THRESH_OTSU)
        #Contours Targets
        contours, hierarchy = cv2.findContours(thrT,cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
        contours = sorted(contours, key=cv2.contourArea,reverse=True) 
        cv2.drawContours(blurT, contours, -1, (0,255,0), 0)
        perimeters = [cv2.arcLength(contours[i],True) for i in range(len(contours))]   
       # Morphology to eliminate islands to Targets   
        resT = cv2.bitwise_and(blurT,blurT, mask= maskT)
        kernelT = np.ones((15,15),np.float32)/225
        openingT = cv2.morphologyEx(resT, cv2.MORPH_OPEN, kernelT)
        erosionT = cv2.erode(openingT,kernelT,iterations = 1)
        dilationT = cv2.dilate(erosionT,kernelT,iterations = 1)
        momentsT = cv2.moments(maskT)
        areaT = momentsT['m00']
        if(areaT > 20):
            xT = int(momentsT['m10']/momentsT['m00'])
            yT = int(momentsT['m01']/momentsT['m00'])
            print ("xT = ", xT)
            print ("yT = ", yT)
        else:
            xT, yT = -255, -255
        sd.putNumber("errorTargets", xT)

        k = cv2.waitKey(20) & 0xFF
        if k == 27:
            break
