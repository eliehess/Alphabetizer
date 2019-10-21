# TCG Alphabetizer

This application provides a GUI to alphabetize and condense a list of trading cards.

<h3> Usage </h3>

- Type, paste, or otherwise insert valid text (see next bullet for what maxes text "valid") into the left box of the GUI, then click the "Alphabetize" button. 
Your input will be condensed and alphabetized by card name, then displayed in the right box of the GUI.

- Cards must be separated by new lines, and each entry must contain the number of cards, an optional 'x', a space, and the card name.
Assuming the rest of the line is formatted correctly, everything after the number, 'x', and space is treated as part of the card name.

- If one line is invalid, the rest of the input should process normally. *The exception is that no line may contain an ampersand ('&'), as ampersands are used as part of the sorting process.*

- Examples of valid input:

  - >4 Saheeli Rai<br>
    4x Teferi, Time Raveler<br>
    3 Felidar Guardian

  - >4 Saheeli Rai (KLD)<br>
    4x Teferi, Time Raveler \[WAR]<br>
    3 Felidar Guardian \*FOIL*
  
  - >2x Saheeli Rai<br>
    4x Teferi, Time Raveler<br>
    3x Felidar Guardian<br>
    2 Saheeli Rai

- Examples of invalid input:

  - >4Saheeli Rai<br>
    4Teferi, Time Raveler<br>
    3Felidar Guardian
    
  - >4xSaheeli Rai<br>
    4xTeferi, Time Raveler<br>
    3xFelidar Guardian
  
  - >4X Saheeli Rai<br>
     4X Teferi, Time Raveler<br>
     3X Felidar Guardian
      
  - >Saheeli Rai 4<br>
    Teferi, Time Raveler 4<br>
    Felidar Guardian 3

<h3>Clipboard</h3>
- The application has built-in functionality to copy from, and paste to, the system clipboard with the press of a button. <br>
- Simply click the "Paste From Clipboard" button, and if the contents of your clipboard can be parsed into a Java String, the input box will contain the contents of your clipboard.<br>
- Similarly, if you click the "Copy To Clipboard" button, the contents of the outpit box will be copied to your clipboard.<br>
- If you click the "Clipboard Pivot" button, and the contents of your clipboard can be parsed into a Java String, the program will alphabetize your clipboard as if you had pressed "Paste From Clipboard", "Alphabetize", and "Copy To Clipboard", in that order.