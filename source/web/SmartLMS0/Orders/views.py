from django.shortcuts import render, redirect
from django.http import HttpResponse, JsonResponse
from django.contrib.auth.models import User
from Accounts.models import *
from .models import *
import json
from datetime import datetime

from django.views.decorators.csrf import csrf_exempt

@csrf_exempt
def HostelStatus(request):
	if request.method == 'POST':
		dataDict = json.loads(request.body.decode('utf8'))
		name = dataDict['uname']
		token = dataDict['token']

		userInst = User.objects.get(username=name)
		profInst = Profile.objects.get(user=userInst)
		reply = {}

		if profInst.token == token:
			allHstls = Hostel.objects.all()
			for hstl in allHstls:
				reply[hstl.hNum] = hstl.currLoad

		JsonReply = JsonResponse(reply)
		print(JsonReply)
		print(type(JsonReply))
		return HttpResponse(JsonReply)

@csrf_exempt
def PlaceOrder(request):
	if request.method == 'POST':
		dataDict = json.loads(request.body.decode('utf8'))
		name = dataDict['uname']
		token = dataDict['token']
		# imgs = dataDict['pics']

		userInst = User.objects.get(username=name)
		profInst = Profile.objects.get(user=userInst)
		hostInst = Hostel.objects.get(hNum=dataDict['hnum'])
		reqstat = 0
		position = -1
		reply = {}

		if profInst.token == token:
			newOrder = Order(user=profInst,
							 hostel=dataDict['hnum'],
							 reqstat=reqstat,
							 position=position,
							 # timestamp=datetime.now(),
							 shirts=dataDict['shirts'],
							 pants=dataDict['pants'],
							 towels=dataDict['towels'],
							 bedsheets=dataDict['bedsheets'],
							 innerwears=dataDict['innerwears'],
							 shorts=dataDict['shorts'],
							 tshirts=dataDict['tshirts'],
							 others=dataDict['others'],
							)
			newOrder.save()
			reply['reqid'] = newOrder.id
			reply['reqstat'] = reqstat
			reply['position'] = position

		JsonReply = JsonResponse(reply)
		print(JsonReply)
		print(type(JsonReply))
		return HttpResponse(JsonReply)

@csrf_exempt
def Status(request):
	# NOTE - only implemented for getting status of 1 request.
	#		 To be extended for DB syncing.
	if request.method == 'POST':
		dataDict = json.loads(request.body.decode('utf8'))
		name = dataDict['uname']
		token = dataDict['token']
		reqids = dataDict['reqids']

		userInst = User.objects.get(username=name)
		profInst = Profile.objects.get(user=userInst)
		# ordInst = Order.objects.all().filter(id=reqid)
		reply = []

		if profInst.token == token:
			for reqid in reqids:
				ordInst = Order.objects.get(id=reqid)
				reply.append({'reqid': reqid, 'sta': ordInst.reqstat, 'position': ordInst.position})

		JsonReply = JsonResponse(reply, safe=False)
		print(JsonReply)
		print(type(JsonReply))
		return HttpResponse(JsonReply)

@csrf_exempt
def History(request):
	if request.method == 'GET':
		dataDict = json.loads(request.body.decode('utf8'))
		name = dataDict['uname']
		token = dataDict['token']
		num = dataDict['number']

		userInst = User.objects.get(username=name)
		profInst = Profile.objects.get(user=userInst)
		reply = {'reqids': []}

		if profInst.token == token:
			reqOrders = Order.objects.all().filter(user=profInst, id__gte=num)
			for order in reqOrders:
				reply['reqids'].append(order.id)

		JsonReply = JsonResponse(reply)
		print(JsonReply)
		print(type(JsonReply))
		return HttpResponse(JsonReply)
