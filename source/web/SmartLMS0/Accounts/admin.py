"""
This module is used to register the models (either in-built OR custom) to the admin site, so that
the backend site administrator can access, update and delete the records in the database table.
"""

from django.contrib import admin

# import the 'Profile' & 'Hostel' models from models.py
from .models import Profile, Hostel

# register the models to the admin site
admin.site.register(Profile)
admin.site.register(Hostel)
