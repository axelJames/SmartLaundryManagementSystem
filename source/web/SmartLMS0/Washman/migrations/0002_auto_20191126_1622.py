# Generated by Django 2.2.7 on 2019-11-26 16:22

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('Washman', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='washman',
            name='hostel',
            field=models.OneToOneField(on_delete=django.db.models.deletion.CASCADE, to='Accounts.Hostel'),
        ),
    ]
