# Team 2 - YouTube Trending Video Analysis
This is the project repo for CSYE7200 Fall 2022 <br>
Team Members: Deepika Balasubramanian, Binghui Lai, Siyuan Xu, Weikun Zhuang

Repo outline:
| Folder | Description |
|--|--|
| *YouTube API Data Retrieval* | Python code to retrieve trending video information from YouTube API |
| *Web Interface* | An web interface that has 2 indexes. One showcases the results of prediction of like counts, the other lists today's (presentation day) trending videos |
| *Visualization* |  Graphs and dashboard generated from Databricks notebook that shows the category counts, video statistics trends, and hashtags word cloud over the past week and past month |
| *Presentation* | Contains the presentation documents |

## Project Outline
![alt text](https://github.com/wkzhuang/YouTube-Analysis/blob/main/Visualization/workflow.png?raw=true)

## Steps to Run the Project
### Data File Instructions
The supplementary Kaggle dataset we used is over 200MB and exceeds the file limit. Please download the csv file from Google Drive here:
https://drive.google.com/file/d/1IY97XLu3CQJaIUuM38-ptdkvyeXCpxDc/view?usp=share_link

Make sure you put the file in two of the folders:
* In _YouTube API Data Retrieval_ folder directly
* In _Web Interface_ folder, put in app/resources

### Data Retrieval From YouTube API Data 
If you haven't installed Google Client API, you can do before running the script:
```bash
pip install --upgrade google-api-python-client
```
(To create a new API key, create a new Google Cloud Project following the instructions here https://developers.google.com/workspace/guides/create-project 
and enable YouTube Data API v3 in your workspace. In APIs and Services, go to Credentials and create new API key.)
To retrieve trending video information and save to csv, run:
```bash
python3 Trending_Video_Update.py
```
The current day's data will be saved both to a separate csv file, and to the accumulated file (adding to Kaggle dataset mentioned above).

### Web Interface on Play Framework
Run the following command to start the application
```bash
sbt run
```
Or, in IntelliJ's sbt shell, input "run".

Once it's done compiling, go to http://localhost:9000 in web browser. <br>

The first index shows the trending video list on presentation day. (Generated csv was put in Resources folder) <br>

The second index shows the prediction of average like counts in each category the next day. <br>

The preprocessing and model training is incorporated into the application. It reads Data from CSV files, preprocess and train a machine learning model on the fly while user selects different category to predict.

In more details, the model predicts the average like counts of the next day in each category based on trending data in the past two years.
After category input by user from UI, a category will be sent to scala and the model will pick the right data to process, train and predict.
Although limited, linear regression is applied in this project. It's quick in training and easy to modify, but suffers from overfitting in time series data


### Visualization
Login to Databricks community edition. Navigate to the floating menu on the left side, select **Workspace**. Under workspace, from left to right, choose **Workspace -> Users**. Next to account email address, click the small down arrow and select **Import**. Import the *youtube_analysis_vis.html* file in the *"Visualization"* folder. <br>

If needed to re-run every cell in the entire notebook, it will be required to create a new cluster and attach to the notebook in order to run. On the upper right corner, next to "Run All", click and create a new resource. Runtime version is 10.4 LTS (includes Apache Spark 3.2.1, Scala 2.12).
If the shared upload file path doesn't work, manually upload the csv files by going to **File -> Upload Data to DBFS**


## Dashboard Results
![alt text](https://github.com/wkzhuang/YouTube-Analysis/blob/main/Visualization/dashboard.png?raw=true)
