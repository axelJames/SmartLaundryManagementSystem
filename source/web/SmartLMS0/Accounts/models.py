from django.db import models
from django.contrib.auth.models import User
from django.db.models.signals import post_save
from django.dispatch import receiver

class Hostel(models.Model):
    hNum = models.IntegerField()
    hName = models.CharField(max_length=20, default=None)
    thresh = models.IntegerField()
    currLoad = models.IntegerField()

    def __str__(self):
        return 'H-' + str(self.hNum) + ' ' + self.hName


class Profile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    token = models.TextField(default=None, blank=True, null=True)      # to accept null values => TextField(default=None, blank=True, null=True)
    ldap = models.CharField(max_length=15)
    hostel = models.ForeignKey(Hostel, on_delete=models.CASCADE)
    phone = models.IntegerField()
    dept = models.CharField(max_length=15)

    def __str__(self):
        return self.user.username



# @receiver(post_save, sender=User)
# def create_user_profile(sender, instance, created, **kwargs):
#     if created:
#         Profile.objects.create(user=instance)
#
# @receiver(post_save, sender=User)
# def save_user_profile(sender, instance, **kwargs):
#     instance.profile.save()
