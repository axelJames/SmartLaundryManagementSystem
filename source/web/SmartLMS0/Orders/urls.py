from django.urls import path
from . import views

app_name = 'Orders'

urlpatterns = [
    path('hostelStatus/', views.HostelStatus, name='hostelStatus'),
    path('order/', views.PlaceOrder, name='placeOrder'),
    path('status/', views.Status, name='status'),
    path('history/', views.History, name='history'),

    # # experimental... delete it & uncomment above url
    # path('login/', views.HostelStatus, name='login'),
]