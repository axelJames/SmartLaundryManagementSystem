# Generated by Django 2.2.7 on 2019-11-26 06:36

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Orders', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='order',
            name='shorts',
            field=models.IntegerField(default=0),
        ),
        migrations.AddField(
            model_name='order',
            name='tshirts',
            field=models.IntegerField(default=0),
        ),
    ]
