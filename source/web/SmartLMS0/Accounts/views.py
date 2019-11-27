from django.http import HttpResponse, JsonResponse
from django.shortcuts import render, redirect
from django.contrib.auth.forms import AuthenticationForm
from django.contrib.auth import login, authenticate, logout
from .forms import UserSignUpForm, UserProfileForm
from django.contrib.auth.models import User
from .models import *
import json
import secrets

from django.views.decorators.csrf import csrf_exempt

def signup_view(request):
    if request.method == 'POST':
        form = UserSignUpForm(request.POST)
        profile_form = UserProfileForm(request.POST)
        if form.is_valid() and profile_form.is_valid():
            user = form.save()
            profile = profile_form.save(commit=False)
            profile.user = user
            profile.save()
            return redirect('homepage')
    else:
        form = UserSignUpForm()
        profile_form = UserProfileForm(request.POST)

    context = {'form': form, 'profile_form': profile_form}
    return render(request, 'Accounts/signup.html', context)

@csrf_exempt
def login_view(request):
    

    if request.method == 'POST':
        dataDict = json.loads(request.body.decode('utf8'))
        name = dataDict['uname']
        pswd = dataDict['password']

        status = 0

        token = secrets.token_hex(32)
        userInst = User.objects.all().filter(username=name)
        reply = {'status': status}

        if status == 1:     # Invalid user credentials
            print('Nothing to be done')

        elif userInst.exists():     # user entry is already present is database
            profInst = Profile.objects.get(user=userInst[0])
            hostInst = profInst.hostel

            reply['hnum'] = hostInst.hNum
            reply['load'] = hostInst.currLoad
            reply['token'] = token
            profInst.token = token
            profInst.save()

        else:   # user is logging-in 1st time => create a user instance
            # get user info from LDAP
            hnum = 1
            ldap = 987
            hostInst = Hostel.objects.get(hNum=hnum)
            phone = 987
            dept = 'XYZ'

            newUser = User(username=name)
            newProfile = Profile(user=newUser, token=token, ldap=ldap, hostel=hostInst, phone=phone, dept=dept)
            newUser.save()
            newProfile.save()

            reply['hnum'] = hnum
            reply['load'] = hostInst.currLoad
            reply['token'] = token

        JsonReply = JsonResponse(reply)
        print(JsonReply)
        print(type(JsonReply))
        return HttpResponse(JsonReply)

        # return HttpResponse('Testing...')

        # print('req reached')
        # form = AuthenticationForm(data=request.POST)
        # if form.is_valid() or True:
        # status = 0

        # uname = form.get_user()
        # userInst = User.objects.get(username=uname)
        # hnum = Profile.objects.get(user=userInst).hostel.hNum
        # load = Hostel.objects.get(hNum=hnum).currLoad
        # reply = {
        #     'status': status,
        #     'hnum': hnum,
        #     'load': load
        # }
        # JsonReply = JsonResponse(reply)
        # # JsonStr = json.dumps(reply)

        # login(request, uname)
        # return HttpResponse(request.body)
        # if 'next' in request.POST:
        #     return redirect(request.POST.get('next'))
        # else:
        #     return redirect('homepage')
    else:
        return render(request, 'Accounts/login.html')

def logout_view(request):
    if request.method == 'POST':
        logout(request)
        return redirect('homepage')
