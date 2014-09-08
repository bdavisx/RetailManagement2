
Let's start with a simple splash screen. We will only need to build some simple one way bindings. The screen will 
only need three properties and an optional fourth:

    1. Title
    2. Details about what is currently going on
    3. Progress Bar showing how many steps out of the total startup steps are completed
    4. Progress Bar showing the current progress of the current startup step (optional)

We will start even simpler though, we will have a 3 property splash screen that basically passes everything thru.
We don't want to end up with a pass-thru for the progress bar stuff. The progress bar takes a double value going
from 0.0 to 1.0 to display progress. That's not how we want the View Model to end up working, we want the View 
Model to be something that works logically from the application's standpoint, not tied to how the UI controls
work. We want the application to be able to set the number of steps it needs to complete, then be able to set how
many steps it has completed.  
