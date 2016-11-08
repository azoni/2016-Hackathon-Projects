from django.contrib import admin

from .models import Choice, Question


class ChoiceInline(admin.TabularInline ):
    model = Choice
    extra = 3


class QuestionAdmin(admin.ModelAdmin):
    fieldsets = [
        (None,               {'fields': ['questionText']}),
        ('Date information', {'fields': ['pubDate'], 'classes': ['collapse']}),
    ]
    inlines = [ChoiceInline]
    list_display = ('questionText', 'pubDate', 'publishedRecently')
    list_filter = ['pubDate']
    search_fields = ['questionText']
    admin.site.site_header = 'Quizler'

admin.site.register(Question, QuestionAdmin)