# -*- coding: utf-8 -*-
# Generated by Django 1.10 on 2016-08-23 01:50
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('polls', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='question',
            old_name='questonText',
            new_name='questionText',
        ),
    ]