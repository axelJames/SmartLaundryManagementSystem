from django.db import models
from django.contrib.auth.models import User
from Accounts.models import *
from django.db.models.signals import post_save
from django.dispatch import receiver

REQ_STATUS = [tuple([x,x]) for x in range(0,5)]

class Order(models.Model):
    user = models.ForeignKey(Profile, on_delete=models.CASCADE)
    timestamp = models.DateTimeField(auto_now_add=True)
    hostel = models.IntegerField()
    reqstat = models.IntegerField(choices=REQ_STATUS)
    position = models.IntegerField()

    shirts = models.IntegerField()
    pants = models.IntegerField()
    towels = models.IntegerField()
    bedsheets = models.IntegerField()
    innerwears = models.IntegerField()
    shorts = models.IntegerField()
    tshirts = models.IntegerField()
    others = models.IntegerField()

    def __str__(self):
    	return self.user.user.username + ' ' + str(self.timestamp.date()) + ' ' + str(self.timestamp.hour) + ':' + str(self.timestamp.minute)


class OrderImages(models.Model):
    order = models.ForeignKey(Order, on_delete=models.CASCADE)
    img = models.ImageField()
