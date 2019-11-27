from django.urls import path
from . import views

app_name = 'Washman'

urlpatterns = [
    path('login/', views.WashmanLogin, name='washmanlogin'),
    path('pendingrequests/', views.PendingRequests, name='pending'),
    path('approvedrequests/', views.ApprovedRequests, name='approved'),
    path('reqdetails/', views.RequestDetails, name='reqdetails'),
    path('changestatus/', views.ChangeStatus, name='changestatus')

    # # experimental... delete it & uncomment above url
    # path('login/', views.HostelStatus, name='login'),
]
