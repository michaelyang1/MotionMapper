# MotionMapper
## How to build the app
1) Clone the repository 
2) Open the project up in Android Studio (the project name is MotionMapper)
3) Install the Android AVD emulator
4) Once the gradle sync is complete, hit the green play button to compile the app
## How to use the app 
1) Upon first using the app, give permission for the app to monitor phone calls
2) Tap the three dots to open advanced settings for the emulator (last option)
3) Select Phone and click "Call Device" to make a call to the emulator (make sure airplane mode is turned off)
4) While the phone is ringing, tap on "Virtual sensors" and adjust the x, y, and z rotations of the phone to simulate a user picking up their device for a call. 
5) Switch logcat type to "Error" to see progress of the data collection 
6) Upon data collection conclusion, go into Android Studio's View >> Tool Windows >> Device File Explorer >> sdcard >> Android >> data >> com.example.motionmapper >> files >> MotionsDirectory >> motions.txt
7) Note motions.txt will be generated or altered should the phone's final position be in an active calling position as opposed to lying flat. 
## How to read Motions.txt 
* Every line represents the phone's acclerometer values at a given time point
* Each line consists of the phone's x, y, and z-axis accleration forces in that order 
* Comma delimited 
## Further considerations
* If we wanted to collect data samples from millions of users, the best way to transfer data would be through a cloud database platform such as AWS or Firebase. Cloud databases are especially adept at scaliabilty and fast access times, leading to efficient data transfers. 
* To ensure the data transfer is secure, we would encrypt the files containing the motion data and use reputable APIs to securely transfer our data to the cloud. The API keys need also to be stored securely, such as using the Native Development Kit, whose libraries are much harder to decompile. 
* If I had more time, I probably would have collected gyroscopic sensor data as well to measure the device's angular velocity. I also would have found a way to end the sensor data collection immediately after the user picked up his/her phone as opposed to collecting data until the prescribed time expired. 
* I encountered a few issues with this project. The first was detecting phone calls, specifically when the phone started ringing and when the user picked up or the call ended, and figuring out how to collect my sensor data during this interval. I also had difficulty simulating someone picking up their phone to answer a call in the Android emulator itself. Writing the data to a file on the device proved somewhat challenging due to the various permissions required by Android.
## Images
<img width="1321" alt="shot1" src="https://user-images.githubusercontent.com/60278399/140010587-b806bbc0-42b3-4c92-98cf-450649dc89a8.png">
<img width="1332" alt="shot2" src="https://user-images.githubusercontent.com/60278399/140010592-f0d2fb8b-b577-4a60-b185-9fb39822e55b.png">
<img width="1754" alt="shot3" src="https://user-images.githubusercontent.com/60278399/140010597-02161dee-22a8-41f4-ac41-542cd11f070c.png">

