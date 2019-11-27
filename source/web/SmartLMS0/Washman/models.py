from django.db import models
from django.contrib.auth.models import User
from Accounts.models import *

class WashMan(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    token = models.TextField(default=None, blank=True, null=True)
    hostel = models.OneToOneField(Hostel, on_delete=models.CASCADE)
    phone = models.IntegerField()

    def __str__(self):
        return self.user.username
