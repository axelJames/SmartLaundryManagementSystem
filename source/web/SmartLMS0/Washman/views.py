from django.shortcuts import render, redirect
from django.http import HttpResponse, JsonResponse
from django.contrib.auth.forms import AuthenticationForm
from django.contrib.auth import login, authenticate, logout
from django.contrib.auth.models import User
from .models import *
from Orders.models import *
from Washman.models import *
import json
import secrets

from django.views.decorators.csrf import csrf_exempt

@csrf_exempt
def WashmanLogin(request):
    if request.method == 'POST':
        dataDict = json.loads(request.body.decode('utf8'))
        name = dataDict['uname']
        pswd = dataDict['password']
        token = secrets.token_hex(32)
        reply = {}

        user = authenticate(username=name, password=pswd)
        if user is not None:
            washmanInst = WashMan.objects.get(user=user)
            reply['status'] = 0
            reply['token'] = token
            washmanInst.token = token
            washmanInst.save()

        else:
            reply['status'] = 1

        JsonReply = JsonResponse(reply)
        print(JsonReply)
        print(type(JsonReply))
        return HttpResponse(JsonReply)

@csrf_exempt
def PendingRequests(request):
    if request.method == 'POST':
        dataDict = json.loads(request.body.decode('utf8'))
        # uname --> wash man --> hostel --> waiting requests
        name = dataDict['uname']
        token = dataDict['token']

        userInst = User.objects.get(username=name)
        washmanInst = WashMan.objects.get(user=userInst)
        hostNum = washmanInst.hostel.hNum
        reply = {'status': 1, 'requests': []}

        if washmanInst.token == token:
            reply['status'] = 0
            reqOrders = Order.objects.all().filter(hostel=hostNum, reqstat=0)
            for order in reqOrders:
                reply['requests'].append([order.id, order.user.user.username])

        JsonReply = JsonResponse(reply)
        print(JsonReply)
        print(type(JsonReply))
        return HttpResponse(JsonReply)

@csrf_exempt
def ApprovedRequests(request):
    if request.method == 'POST':
        dataDict = json.loads(request.body.decode('utf8'))
        # uname --> wash man --> hostel --> waiting requests
        name = dataDict['uname']
        token = dataDict['token']

        userInst = User.objects.get(username=name)
        washmanInst = WashMan.objects.get(user=userInst)
        hostNum = washmanInst.hostel.hNum
        reply = {'status': 1, 'requests': []}

        if washmanInst.token == token:
            reply['status'] = 0
            reqOrders = Order.objects.all().filter(hostel=hostNum, reqstat=1)
            for order in reqOrders:
                reply['requests'].append([order.id, order.user.user.username])

        JsonReply = JsonResponse(reply)
        print(type(JsonReply))
        print(JsonReply)
        return HttpResponse(JsonReply)

@csrf_exempt
def RequestDetails(request):
    if request.method == 'POST':
        dataDict = json.loads(request.body.decode('utf8'))
        name = dataDict['uname']
        token = dataDict['token']
        reqid = dataDict['reqid']

        userInst = User.objects.get(username=name)
        washmanInst = WashMan.objects.get(user=userInst)
        ordInst = Order.objects.get(id=reqid)
        reply = {}

        if washmanInst.token == token:
            reply['uname'] = ordInst.user.user.username
            reply['timestamp'] = ordInst.timestamp
            reply['hostel'] = ordInst.hostel
            reply['reqstat'] = ordInst.reqstat
            reply['position'] = ordInst.position
            reply['shirts'] = ordInst.shirts
            reply['pants'] = ordInst.pants
            reply['towels'] = ordInst.towels
            reply['bedsheets'] = ordInst.bedsheets
            reply['innerwears'] = ordInst.innerwears
            reply['shorts'] = ordInst.shorts
            reply['tshirts'] = ordInst.tshirts
            reply['others'] = ordInst.others

        JsonReply = JsonResponse(reply)
        print(type(JsonReply))
        print(JsonReply)
        return HttpResponse(JsonReply)

@csrf_exempt
def ChangeStatus(request):
    if request.method == 'POST':
        dataDict = json.loads(request.body.decode('utf8'))
        name = dataDict['uname']
        token = dataDict['token']
        reqid = dataDict['reqid']
        newstat = dataDict['newstat']

        userInst = User.objects.get(username=name)
        washmanInst = WashMan.objects.get(user=userInst)
        hostInst = washmanInst.hostel
        ordInst = Order.objects.get(id=reqid)
        reply = {'success': 0}

        if washmanInst.token == token:
            if newstat == 1 and hostInst.currLoad < hostInst.thresh:    # change reqstat from 0 to 1, when currLoad < thresh
                reply['success'] = 1
                hostInst.currLoad += 1
                hostInst.save()

                ordInst.reqstat = newstat
                ordInst.position = hostInst.currLoad
                ordInst.save()

            elif newstat == 2:      # this means reqstat changed from 0 to 2
                reply['success'] = 1
                ordInst.reqstat = newstat
                ordInst.save()

            elif newstat == 3:      # this means reqstat changed from 1 to 3
                reply['success'] = 1
                hostInst.currLoad -= 1
                hostInst.save()

                ordInst.reqstat = newstat
                ordInst.position = -1
                ordInst.save()

        JsonReply = JsonResponse(reply)
        print(type(JsonReply))
        print(JsonReply)
        return HttpResponse(JsonReply)
