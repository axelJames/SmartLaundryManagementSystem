# Generated by Django 2.2.7 on 2019-11-26 06:36

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('Orders', '0002_auto_20191126_0636'),
    ]

    operations = [
        migrations.AlterField(
            model_name='order',
            name='shorts',
            field=models.IntegerField(),
        ),
        migrations.AlterField(
            model_name='order',
            name='tshirts',
            field=models.IntegerField(),
        ),
    ]