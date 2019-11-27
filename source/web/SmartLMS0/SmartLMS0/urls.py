from django.contrib import admin
from django.urls import path, include
from . import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('accounts/', include('Accounts.urls')),
    path('laundry/', include('Orders.urls')),
    path('washman/', include('Washman.urls')),

    # experimental... delete it & uncomment above 2 urls
    # path('accounts/', include('Orders.urls')),

    path('', views.homepage, name="homepage"),
]
