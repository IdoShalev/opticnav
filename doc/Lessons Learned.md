Legacy Document
===============

The following document containing all lessons learned from all team members and future enhancements.

Lessons Learned
---------------

###**Danny**###

####Communication barriers are bad!####

>When working in a team on any project in the future, I think communication should be as open as possible. The harder it is to communicate with your team members, you're more likely to withhold valuable information from others.
This is addressed in the software development methodology. How easy is it to commit code changes, to add documentation, report bugs, or notifying team members of your progress? What are the barriers? How does that affect the project outcome?

####Test, test, test!####

>I've known the importance of testing for a long time, but the importance of it really shines here. The importance of unit testing and integration testing your software cannot be understated. Even when testing is "inconvenient", it should be among the first things to be done. Even if it kills you.

####Test-driven Development####
>*In short: Write tests for a component before writing the component being tested.*
When designing a system, I found it really helps to write code that uses yet-to-be-defined classes and methods. This is because as a programmer, you are only focused on the API of your components and not what the implementation is. With test-driven development, it's easier to determine the "completeness" of your components and if they operate correctly.

####Testing unknowns####
>It takes a lot of bravery to rely on how you think a subsystem might work before using it in the implementation.
One should always, or really try to, be familiar with the components needed before relying on them.

####It's very difficult to define the entire system before writing your first line of code####

>It's a consequence of the Waterfall methodology. It's easier to determine the ultimate structure of a system if it's a type of system that you've have a hand in designing before. However, if there is a realm of (un)known unknowns, it's either very difficult or, dare I say, impossible.
The unknowns: Android development, GPS capabilities, network design patterns, and more that I'm forgetting

####Boilerplate really does affect design####

>Languages with lots of boilerplate, such as Java, really do encourage bad shortcuts. Boilerplate leads to lots of non-productive typing, which leads to lots of visual noise, which leads to burnout, which leads to taking mental shortcuts.
For example, interactive applications require lots of callbacks in order to avoid hanging the UI thread with long-running tasks. Java is extremely verbose with defining callback methods, by requiring interface declarations for each and every unique callback.
If you define multiple layers of your application (UI frontend, UI backend, network backend), you may have many, MANY, callback interfaces. There's probably at least 20-30 interfaces in the project dedicated to providing callbacks.
Having that much code hurts your head, so you might attempt to reduce the amount by reusing components that happen to be similar (but do not serve the same purpose).
Java is not an inherently flawed language, but it's definitely not suitable for certain types of applications. I might recommend something more lax like C (yes, C), Python, or JavaScript.

###**Ido**###

>Going back to first semester, all the diagrams and documents we wrote really seemed to narrow down the scope of the project and help us figure out what we are going to do. Unlike the other groups, our project didn't have a scope which was a bit harder to start with, we were only told to make something cool with an augmented reality device. The planning helped figure out better what we were going to do, however most of the diagrams we had to do weren't so helpful. Overall I thought while the planning was important, it also took too long, and some of the requirements for the documents weren't really looked at when we started coding. Danny did go the extra mile to make a wiki for us which helped a lot by allowing everyone to add what information they found to be useful and look over the document anywhere we had internet. Danny also decided to build a VM for us to work on, which means everyone has their own space, but the same build and software.

>The management of the team could have been better, as I am sure was the case with a lot of team, not everyone had a lot of experience working on such big project with a team for such a long time. I thought the Capstone was a great way to see it and learn from it. Time management could also gone better, we found that we pushed back a lot of due dates and we haven't thought of everything when we made our Gantt Chart. Overall we did fine, we managed to finish a lot over the small amount of time we got compared to the big project which we had to write.

>For future projects I would highly recommend working with a VM and if possible creating a wiki for the document as well, the VM and wiki really helped us and were worth the time spent working on them. As for design document, I would probably work on that while starting the project, while it did help solve some issues before we started working on coding, a lot of it changed when we started coding and a lot of the design wasn’t used at the end. I would also work on team management and time management better, create a proper Gantt Chart and assign due dates that are a little more realistic. Finally I would make sure to document as I go better, I haven’t documented my code as I was codding, I could have saved more time if I had done so.

###**Jacky**###

>As long and arduous as planning was, it at least allowed us to imagine the system we were trying to build. That being said, I found the finished design document was not that useful, only referencing it a few times during coding. Going back with more knowledge of how a project should be structured, the design document would have been a lot more useful. 

>Asking for help is almost required, it is better to ask for help rather than miss deadlines. Good teamwork and collaboration made the entire coding process a lot smoother. Making sure to tell each other about potential code changes to prevent commit conflicts allowed us to work with less worry. Peanut butter is a great breakfast addition, and it has really helped me fuel up in the mornings.

###**Kay**###

>The most important thing I have learned while working on this project is that waterfall development is a bad thing. I think it would have benefited us more if we had tried some basic concepts of or project while in the design process to see if and how something will work. In the end the only useful thing in the design was the basic concept and scope of our project, most other things changed multiple times during the actual coding period. The requirements gathering phase was probably the most useful thing we did during design but since the requirements of our project are very open ended it was hard for us to get started. Another thing I learned is that team meetings are important. Sadly our team is not very good at holding meetings but that was countered by good teamwork and Danny’s experience. Generally I think having regular review meetings that clarify what everyone is doing and determine the overall progress of the project would have been helpful. This would have made the weekly progress reports as well as keeping the gantt chart easier. I also learned that it is essential to have at least one person (Danny) who is familiar with all parts of the project. This person can then assign work to people and help if problems arise ( since that person basically knows who did what and who is best suited to solve or help solve the problem).
Some technical skills I have learned are mariaDB (mysql) how to use it with java. I also learned the basic concepts about git and svn. Furthermore my Linux skills have improved because we had an Ubuntu VM set up to develop on.
My recommendation for future projects is to try out some basic concepts of the project while in the design phase, especially if you are using technologies you are unfamiliar with. Another one is to go into more detail off how things should work/be structured. While thinking about the specific classes and methods is hard one should still ask how things will actually communicate/work together. Probably the best thing to do is to just start coding since even just a little bit of code can help you greatly with the design.

Future Enhancements
---------
* Network protocol is severely broken --- redesign it
* Better location services, such as WPS
* Web application and network security
* Web administrator tools -- Manage all ARDs, remove accounts
* Use web application as a guest --- no registration
* Ping ARD from web application
* Mark current location from the device
* Remove a user from an instance
* Provide images and descriptions for markers
* Simpler and easier map creation
* Message boxes having an clear way to remove them, rather than having them time out