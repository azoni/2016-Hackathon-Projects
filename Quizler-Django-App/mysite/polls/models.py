import datetime
from django.utils import timezone
from django.db import models
#from django.utils.encoding import python_2_unicode_comptible

# Create your models here.

class Question(models.Model):
    questionText = models.CharField(max_length = 200)
    pubDate = models.DateTimeField('date published')

    def __str__(self):
        return self.questionText

    def publishedRecently(self):
        now = timezone.now()
        return now - datetime.timedelta(days=1) <= self.pubDate <= now
        
    publishedRecently.admin_order_field = 'pubDate'
    publishedRecently.boolean = True
    publishedRecently.short_description = 'Published recently?'

class Choice(models.Model):
    question = models.ForeignKey(Question, on_delete = models.CASCADE)
    choiceText = models.CharField(max_length = 200)
    votes = models.IntegerField(default = 0)

    def __str__(self):
        return self.choiceText