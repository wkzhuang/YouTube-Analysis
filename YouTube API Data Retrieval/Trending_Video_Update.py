#!/usr/bin/env python
# coding: utf-8

# In[33]:


import pandas as pd
import os
from datetime import date
from google.auth.transport.requests import Request
from googleapiclient.discovery import build


# In[34]:


#Reading cumulate trending video
Cumulate_trending = pd.read_csv('US_youtube_trending_data.csv',engine='python')
Cumulate_trending.drop('Unnamed: 0',axis=1,inplace=True)


# In[35]:


api_key = 'AIzaSyDpGpr3c_fQAHhTWt3kcDjur07RbbsLRZc'

youtube = build('youtube','v3', developerKey = api_key)

request = youtube.videos().list(
    part="snippet,contentDetails,statistics",
    chart="mostPopular",
    regionCode="US"
)
response = request.execute()


# In[36]:


today = date.today().strftime('%Y-%m-%d')

#Creating df
df = pd.DataFrame(columns=['video_id', 'publishedAt', 'title', 'description', 'channelId',
       'channelTitle', 'tags', 'dimension', 'definition', 'caption',
       'licensedContent', 'view_count', 'likes', 'comment_count',
       'favoriteCount', 'categoryId', 'category','trending_date'])

category_list = pd.read_csv('category_list.csv')


# In[37]:


# A function update trending video daily, transforming from json format to pandas dataframe
#Parameter:
    #df: Empty DataFrame to store data
    #response: the first page of trending video retrieve by API.
    #No out put value, the df will be directly updated.
    #Don't forget to output the dataframe to csv

def Trending_Daily_Update(df,response):
    for i in range(len(response['items'])):
        try:
            likeCount = response['items'][i]['statistics']['likeCount']
        except:
            likeCount = None
        
        try:
            commentCount = response['items'][i]['statistics']['commentCount']
        except:
            commentCount = None
        
        try:
            favoriteCount = response['items'][i]['statistics']['favoriteCount']
        except:
            favoriteCount = None
            
        try:
            tags = response['items'][i]['snippet']['tags']
        except:
            tags = None
        
        df.loc[len(df.index)] = [response['items'][i]['id'],
                          response['items'][i]['snippet']['publishedAt'],
                          response['items'][i]['snippet']['title'],
                          response['items'][i]['snippet']['description'],
                          response['items'][i]['snippet']['channelId'],
                           response['items'][i]['snippet']['channelTitle'],
                           tags,
                           response['items'][i]['contentDetails']['dimension'],
                           response['items'][i]['contentDetails']['definition'],
                           response['items'][i]['contentDetails']['caption'],
                           response['items'][i]['contentDetails']['licensedContent'],
                           response['items'][i]['statistics']['viewCount'],
                           likeCount,
                           commentCount,
                           favoriteCount,
                           response['items'][0]['snippet']['categoryId'],
                           category_list[category_list['id'] == int(response['items'][0]['snippet']['categoryId'])].iloc[0,1],
                           today
                            
                                ]
    if 'nextPageToken' in response.keys():
        nextPageToken = response['nextPageToken']
        request = youtube.videos().list(
            part="snippet,contentDetails,statistics",
            chart="mostPopular",
            regionCode="US",
            pageToken = response['nextPageToken']
            )
        
        response = request.execute()
        Trending_Daily_Update(df,response)
        
    else:
        None


# In[38]:


Trending_Daily_Update(df,response)


# In[39]:


frames = [Cumulate_trending, df]
Cumulate_trending = pd.concat(frames).reset_index()

df.to_csv('%s_Trending_Video.csv'%(today))
Cumulate_trending.to_csv('US_youtube_trending_data.csv')


# In[40]:


Cumulate_trending.columns


# In[ ]:




