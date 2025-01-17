PARKFINDER

Workspace


Github:

•	Repository: https://github.com/diegocaboj/MAD2024_DiegoCabo_LuciaZayas

•	Releases: https://github.com/diegocaboj/MAD2024_DiegoCabo_LuciaZayas/releases

Workspace: https://upm365.sharepoint.com/sites/MobileAppDevelopment_DiegoCabo_LuciaZayas/SitePages/CollabHome.aspx  

[Tracking in Sharepoint](https://upm365.sharepoint.com/sites/MobileAppDevelopment_DiegoCabo_LuciaZayas/SitePages/Tracking.aspx)


Description  
ParkFinder is an application focused on parking in Madrid. It features a map that shows parking locations and your current location. On this map, there is a button that tells you which parking lot is closest to you. ParkFinder also keeps a history of your locations and provides information about the parking lots, such as the street name where they are located, the price, the number of spaces, etc. It stands out from many other applications because you can use it without having to keep your location active, thanks to a feature that allows you to manually enter coordinates and a maximum distance. All the parking lots within that distance from the specified location will be displayed.



Screenshots and navigation  
[Complete Folder](images)  
[Screenshot1](images/readme1.jpg)     
[Screenshot2](images/readme2.jpg)    
[Screenshot3](images/readme3.jpg)    
[Screenshot4](images/readme4.jpg)    
[Screenshot5](images/readme5.jpg)  
[Screenshot6](images/readme6.jpg)    
[Screenshot7](images/readme7.jpg)    
[Screenshots8](images/readme8.jpg)    
[Screenshots9](images/readme9.jpg)    
[Screenshots10](images/readme10.png)    
[Screenshots11](images/readme11.png)    
[Screenshots12](images/readme12.jpg)    
[Screenshots13](images/readme13.jpg)    
[Screenshots14](images/readme14.jpg)    
[Screenshots15](images/readme15.jpg)    
[Screenshots16](images/readme16.jpg)    
[Screenshots17](images/readme17.jpg)   
[Screenshots18](images/readme18.JPG)    
[Screenshots19](images/readme19.JPG)    
[Screenshots20](images/readme20.JPG)   
[Screenshots21](images/readme21.JPG)    


Demo Video   
[Sign In Video](https://upm365.sharepoint.com/:v:/s/MobileAppDevelopment_DiegoCabo_LuciaZayas/ES2AMITHTAhMhLtH5I2WoTYBlzJmnXQ_zdikJhrZFrYbsg?e=FNrdJd)

[Sign In With Google Video](https://upm365.sharepoint.com/:v:/s/MobileAppDevelopment_DiegoCabo_LuciaZayas/EZdL8Kk2ZcNIjb5KntnLfMUBTWHpG7bHe2AXcXvfLbUx1A?e=aZ0jfb)

[Demo Video Extended Version](https://upm365.sharepoint.com/:v:/s/MobileAppDevelopment_DiegoCabo_LuciaZayas/EQ51uChoryVDu_lQqSL0szsBk2gzn6yI8PMHp4AyvTeB4Q?e=4bPCZC)

[Demo Video 1 Minute version](https://upm365-my.sharepoint.com/:v:/g/personal/d_cabo_alumnos_upm_es/EQW1owo1_Q5OhGyMz0LB6dQBhJUFYnwzrDWz-49TBEcpIA?e=Ee5Iat&nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJTdHJlYW1XZWJBcHAiLCJyZWZlcnJhbFZpZXciOiJTaGFyZURpYWxvZy1MaW5rIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXcifX0%3D)

FEATURES  

Functional Features  

Parking Location Search:  
Map View: View parking spots on OpenStreetMap    
List View: Browse parking locations in a list    
Detail View: See detailed info for each parking spot   
Distance Filter: Filter spots based on proximity.  

Shared Preferences:  
Save user settings    

Navigation:  
Bottom Navigation: Easy access to Home, Map, and List sections.  

Data Fetching:  
API Integration: Fetch parking data from Madrid Open Data API using Retrofit. APIs use: https://datos.madrid.es/portal/site/egob/menuitem.c05c1f754a33a9fbe4b2e4b284f1a5a0/?vgnextoid=26e6cc885fcd3410VgnVCM1000000b205a0aRCRD&vgnextchannel=374512b9ace9f310VgnVCM100000171f5a0aRCRD&vgnextfmt=default  
Offline Storage: Use Room database for local data storage.  

Authentication:  
Firebase Authentication: Manage user login and data securely.  

Technical Features
Architecture:  
MVVM: Separation of concerns with Model-View-ViewModel architecture.  

Networking:  
Retrofit: API calls for fetching parking data   
Gson: JSON parsing with Retrofit.  

Database:  
Room Database: Local storage with structured data management.

Location Services:
Geolocator: Real-time user location tracking.

Additional Libraries:  
Firebase: For authentication and real-time data  
OpenStreetMap: Map functionalities.  


How To Use  
This application is aimed at residents of Madrid, but in the future, it will be available to more citizens across Spain. It is compatible with the vast majority of mobile devices.
Guide:

1.	Log in by clicking on "Login".

2.	Enter your username.

3.	If you have location services enabled, click on the map and then click the "Which parking is closest" button to see which parking lot is nearest to you.
   
4.	If you do not want to or cannot enable your location, click on "Collection" and enter coordinates and the maximum distance for which you want to search for parking.
   
5.	Click on the parking lots to get more information.
    
6.	To delete a location, click on "Home", then "Previous Locations", select one of them, and click "Delete Item".
  
7.	You can view your profile information by clicking the three dots in the top right corner of the home screen and selecting "My Profile", or you can log out by clicking "Logout".



Participants
List of MAD developers:

Diego Cabo Jurado (d.cabo@alumnos.upm.es) 

Lucía Zayas Martin (lucia.zayas@alumnos.upm.es)

Workload distribution(50%/50%)



Releases

https://github.com/diegocaboj/MAD2024_DiegoCabo_LuciaZayas/releases
