package com.zybooks.qumaps

import Room
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView


class MainActivity : ComponentActivity() {

    //ok so we need a variable for where we're starting and where we're ending.
    //for both we need the name as a string and the actual room object
    lateinit var startName: String
    lateinit var start: Room
    lateinit var endName: String
    lateinit var end:Room


    //we're also going to have a variable for the RoomList class that has an array of all the room objects and also an array for all the room names
    val roomList: RoomList = RoomList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomList.create() //RoomList doesn't have an on create function that automatically goes because it isn't ever the main activity that's running, so we have to call the create function manually

        start()
    }//onCreate

    fun start(){
        setContentView(R.layout.home_screen)

        val startBtn: Button = findViewById(R.id.buttonGetStarted)

        startBtn.setOnClickListener {
            enterStart()
        }//end on click listener
    }

    fun enterStart(){
        setContentView(R.layout.enter_start)
        val startContinue: Button = findViewById(R.id.buttonStartContinue)
        val dropdown: Spinner = findViewById(R.id.spinnerPickRoom)
        val rooms = roomList.roomNames
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rooms)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdown.adapter = arrayAdapter

        startContinue.setOnClickListener {
            enterDestination()
        }//end on click listener

        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                startName = parent.getItemAtPosition(position).toString()//when you select a start, record that in the start variable from earlier
                start = roomList.findRoomObject(startName)!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //if nothing is selected don't do anything, but we have to have this in here to make the onItemSelectedListener work
            }
        }//end onItemSelected

    }//end enterStart function

    fun enterDestination(){
        setContentView(R.layout.enter_destination)
        val destinationContinue: Button = findViewById(R.id.buttonDestinationContinue)
        val dropdown: Spinner = findViewById(R.id.spinnerPickRoomDestination)
        val rooms = roomList.roomNames
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rooms)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropdown.adapter = arrayAdapter

        destinationContinue.setOnClickListener {
            mapView()
        }//end on click listener

        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                endName = parent.getItemAtPosition(position).toString()//when you select a start, record that in the start variable from earlier
                end = roomList.findRoomObject(endName)!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //if nothing is selected don't do anything, but we have to have this in here to make the onItemSelectedListener work
            }
        }//end onItemSelected

    }//end enterDestination function

    fun mapView(){
        setContentView(R.layout.map_view)

        //we're just going to log the start and end to keep it easy
        Log.d("tag", startName + " = " + start.number)
        Log.d("tag", endName + " = " + end.number)

        var directions = arrayOf<String>()

        //we need to get the floors of both rooms and compare them to see if you need to go up or down and what new floor you have to go to.
        var floorStart: Number
        if(start.number.get(0) == 'B'){
            floorStart = 0
            start.number = start.number.substring(2,3) //so now we want just the room number (ex. B-33 should just be 33)
        } else if (start.number.get(0) == '1'){
            floorStart = 1
        } else if (start.number.get(0) == '2'){
            floorStart = 2
        } else if (start.number.get(0) == '3'){
            floorStart = 3
        } else{ // if the first character of the number is 4 OR if it doesn't have a number (board room or board conference room)
            floorStart = 4
        }

        var floorEnd: Number
        floorEnd = if(end.number.get(0) == 'B'){
            0
        } else if (end.number.get(0) == '1'){
            1
        } else if (end.number.get(0) == '2'){
            2
        } else if (end.number.get(0) == '3'){
            3
        } else{ // if the first character of the number is 4 OR if it doesn't have a number (board room or board conference room)
            4
        }

        //so these are the directions if they don't start on the same floor. It just uses the directions to the elevator and then goes to the elevator and then lists them in reverse to get from the elevator to the room
        if(floorStart != floorEnd) {
            directions = startToElevator(directions)
            if (floorStart > floorEnd) {
                directions += ("Go down in the elevator to floor $floorEnd")
            } else {
                directions += ("Go up in the elevator to floor $floorEnd")
            }
            directions = endToElevator(directions)
        }
        else{ //if they're on the same floor
            Log.d("Floor", "same floor")
            var hallwayStart: Number //1 is the main hallway, 2 is for the 3rd and 4th floors where the main hallway zigzags on the right side, 3 is for the first hallway off the main one on the left side and 4 is for the hallway off that side hallway. I have a map. It makes sense
            var hallwayEnd:Number
            var sideBuildingStart: Boolean //true = right side towards friar's field, false = left side towards C-lot //if they're on opposite sides of the building we can do the same thing we did before, but without actually going in the elevator
            var sideBuildingEnd: Boolean
            var sideHallStart: Number//1 = turn left towards elevator, 2 = turn right towards elevator, 3 = forwards towards elevator (end of hallway)
            var sideHallEnd: Number


            //we can determine which side of the building they're on based on how they turn onto the elevator. If they turn left onto the elevator, they are on the left side fo the building and vice versa.
            if((start.directions[0].equals("left"))){ //if they get into the elevator turning left, they're on the left side of the building
                sideBuildingStart = false
            } else{//if they get into the elevator turning right, they're on the right side of the building
                sideBuildingStart = true
            }
            //and same for the ending room
            if(end.directions[0].equals("left")){
                sideBuildingEnd = false
            }
            else{
                sideBuildingEnd = true
            }

            //so if a room has 3 or less directions, we know they're on the main hallway. The Campus ministry office is also on the main hallway, but has an extra turn because its in the entry way.
            if ((start.directions.size <= 3) || (start.number.equals("114"))){
                hallwayStart = 1
            }
            //if a room has exactly 5 directions we know they're on the first side hallway on the left of the building
            else if (start.directions.size == 5){
                hallwayStart = 3
            }
            //if a room has more than 5 directions, it depends which side of the building they're on.
            else{
                //if they're on the right side of the building, we know the room is on the 2nd or 3rd floors where the hallway zigzags
                if (sideBuildingStart == true){
                    hallwayStart = 2
                }
                //if they're on the left side, it means they're in the side side hallway.
                else{
                    hallwayStart = 4
                }
            }//end else

            //we need to find out what side of the hall they're on
            if(start.directions[start.directions.size-1].equals("left")){ //if they turn left out of the room when going towards the elevator
                sideHallStart = 1
            }
            else if (start.directions[start.directions.size-1].equals("right")){
                sideHallStart = 2
            }
            else{
                sideHallStart = 3
            }

            if(end.directions[end.directions.size-1].equals("left")){ //if they turn left out of the room when going towards the elevator
                sideHallEnd = 1
            }
            else if (end.directions[end.directions.size-1].equals("right")){
                sideHallEnd = 2
            }
            else{
                sideHallEnd = 3
            }

            //and the same thing for the end.
            if ((end.directions.size <= 3) || (end.number.equals("114"))){
                hallwayEnd = 1
                Log.d("hall", "1")
            }
            else if (end.directions.size == 5){
                hallwayEnd = 3
                Log.d("hall", "3")
            }
            else{
                if (sideBuildingEnd == true){
                    hallwayEnd = 2
                    Log.d("hall", "3")
                }
                else{
                    hallwayEnd = 4
                    Log.d("hall", "4")
                }
            }//end else

            //if they're on opposite sides of the elevator, follow the same thing for paths with the elevator, but just don't turn onto the elevator.
            if(sideBuildingStart != sideBuildingEnd) {
                //so just send the directions to the elevator but leave out the first and second ones and put a move forward in the middle (the first and second ones are turn and move forward respectively, and you don't want the 2 move forwards to repeat each other so just take them out and put a new one in.
                Log.d("!!!!!!", "diff sides")
                directions = startToElevatorNoElevator(directions)

                directions += "Move Forward"

                directions = endToElevatorNoElevator(directions)

            }//end if they're on opposite sides
            else{ //if they are on the same side of the building
                Log.d("!!!!!!", "same side")
                //eventually they will sync up and go the same way.
                //we just need to find out when that is. if they're in the same hallway, its just a straight shot.
                if (hallwayStart == hallwayEnd){
                    Log.d("Tag", "same hall")
                    //if they're in the same hallway, we need to see if they're going up stream or down stream so to speak.
                    if(start.number.toInt() < end.number.toInt())  { //the < symbol works because its looking for which one is alphabetically sooner so works for both numbers and the B-# ones for the basement
                        Log.d("Tag", "Upstream")
                        //we need to go upstream (towards higher numbers)
                        if(sideBuildingStart == true){//if we start on the right side of the building
                            Log.d("Tag", "Start on Right")
                            //go towards the elevator
                            if (sideHallStart == 1){
                                Log.d("Tag", "Section 1")
                                directions += "Turn Left"
                            }
                            else if (sideHallStart == 2){
                                Log.d("Tag", "Section 2")
                                directions += "Turn Right"
                            }
                        }//end if on the right side of building
                        else{ //if we are on the left side
                            Log.d("Tag", "Start on Left")
                            //we need to move away from the elevator to go upstream
                            if (sideHallStart == 1){
                                directions += "Turn Right"
                                Log.d("Tag", "Section 1")
                            }
                            else if (sideHallStart == 2){
                                directions += "Turn Left"
                                Log.d("Tag", "Section 1")
                            }
                        }
                        //we don't need to worry about the side 3 rooms because they don't have the extra step of turning, but everyone moves forward.
                        //now that we're heading the right way, we need to move forward
                        directions += "Move Forward"

                        if(sideBuildingEnd == true){//if we end on the right side of the building
                            if (sideHallEnd == 1){
                                Log.d("Tag", "side 1")
                                directions += "Turn Left"
                            }
                            else if (sideHallEnd == 2){
                                Log.d("Tag", "side 2")
                                directions += "Turn Right"
                            }
                        }//end if on the right side of building
                        else{ //if we are on the left side
                            //we need to move away from the elevator to go upstream
                            if (sideHallEnd == 1){
                                directions += "Turn Right"
                            }
                            else if (sideHallEnd == 2){
                                directions += "Turn Left"
                            }
                        }//end if we are on left side of building
                    }//end if going upstream

                    else{
                        Log.d("Tag", "Downstream")
                        //we need to go down stream (towards lower numbers)
                        if(sideBuildingStart == true){//if we start on the right side of the building
                            //go away from the elevator
                            if (sideHallStart == 1){
                                directions += "Turn Right"
                            }
                            else if (sideHallStart == 2){
                                directions += "Turn Left"
                            }
                        }//end if on the right side of building
                        else{ //if we are on the left side

                            if (sideHallStart == 1){
                                directions += "Turn Left"
                            }
                            else if (sideHallStart == 2){
                                directions += "Turn Right"
                            }
                        }
                        //we don't need to worry about the side 3 rooms because they don't have the extra step of turning, but everyone moves forward.
                        //now that we're heading the right way, we need to move forward
                        directions += "Move Forward"

                        if(sideBuildingEnd == true){//if we end on the right side of the building
                            if (sideHallEnd == 1){
                                directions += "Turn Right"
                            }
                            else if (sideHallEnd == 2){
                                directions += "Turn Left"
                            }
                        }//end if on the right side of building
                        else{ //if we are on the left side
                            //we need to move away from the elevator to go upstream
                            if (sideHallEnd == 1){
                                directions += "Turn Left"
                            }
                            else if (sideHallEnd == 2){
                                directions += "Turn Right"
                            }
                        }//end if we are on left side of building
                    }//end if going down stream


                }//end if we are in the same hallway

                //TO DO: if hall 1 and hall 2
                else if(hallwayStart == 1 && hallwayEnd == 2){ //if 1 to 2
                    //find out which way to turn to go towards 2
                    //we don't have to worry about being on the left side of the building because then that would do the go to the elevator, but don't get in path.
                    if(sideHallStart == 1) {//if we're on side 1
                        directions += "Turn Right"
                    }
                    else{ //if we're on side 2 (there is no side 3 in the main hallway
                        directions += "Turn Left"
                    }
                    //we don't have to have a statement for side 3 because there's no extra turn you just jump straight to moving forward

                    //forward, right, forward, left, forward
                    directions += "Move Forward"
                    directions += "Turn Right"
                    directions += "Move Forward"
                    directions += "Turn Left"
                    directions += "Move Forward"
                    //turn into classroom

                    if(sideHallEnd == 1) {//if we're on side 1
                        directions += "Turn Right"
                    }
                    else{ //if we're on side 2
                        directions += "Turn Left"
                    }
                    //we don't have to have a statement for side 3 because there's no extra turn you get move forward to get there and then you're done
                }//end else if start hall 1 and end hall 2
                else if(hallwayStart == 2 && hallwayEnd == 1) {//if 2 to 1
                    //find which way to turn
                    //we dont have to worry about being on the left side of the building because that would be caught and do the go to the elevator but don't get in trick
                    if(sideHallStart == 1){
                        directions += "Turn Left"
                    }
                    else{//else side = 2 (there is no side 3 rooms in hall 2)
                        directions += "Turn Right"
                    }
                    //forward, right, forward, left, forward
                    directions += "Move Forward"
                    directions += "Turn Right"
                    directions += "Move Forward"
                    directions += "Turn Left"
                    directions += "Move Forward"

                    //turn into room
                    if(sideHallEnd == 1){ //if on side 1
                        directions += "Turn Left"
                    }
                    else { //if side = 2
                        directions += "Turn Right"
                    }
                }//end else if start hall 2 and end hall 1
                //TO DO: if hall 1 and hall 3
                else if(hallwayStart == 1 && hallwayEnd == 3) { //if 1 to 3
                    //we know we're on the right side if we weren't it'd do the go to the elevator but don't get in trick
                    //turn out of room
                    if(sideHallStart == 1){// if on side 1
                        directions += "Turn Right"
                    }
                    else if (sideHallStart ==2){ //if on side 2
                        directions += "Turn Left"
                    }
                    //we don't need to worry about side 3 because they just skip the turn and go straight to forwards
                    //forward, right, forward
                    directions += "Move Forward"
                    directions += "Turn Right"
                    directions += "Move Forward"
                    //turn in to room
                    if(sideHallEnd == 1){//if ending on side 1
                        directions += "Turn Right"
                    }
                    else{//if side 2
                        directions += "Turn Left"
                    }
                    //there is no side 3 in hall 3
                }//end start hall 1 end hall 3

                else if(hallwayStart == 3 && hallwayEnd == 1) { //if 3 to 1
                    // find which way to turn
                    if(sideHallStart == 1){
                        directions += "Turn Left"
                    }
                    else{//if side 2 (there is no side 3 in hall 3
                        directions += "Turn Right"
                    }

                    //if they end in a room that's on side 3 its slightly different, so let's deal with that first
                    if (sideHallEnd == 3){
                        directions += "Move Forward"
                        directions += "Turn Right"
                    }
                    else {
                        //forward, left, forward
                        directions += "Move Forward"
                        directions += "Turn Left"
                        directions += "Move Forward"

                        //turn in to room
                        if (sideHallEnd == 1) {
                            directions += "Turn Left"
                        }
                        else if (sideHallEnd == 2) {
                            directions += "Turn Right"
                        }
                    }//end else if not end side 3
                }//end start hall 3 end hall 1


                else if(hallwayStart == 1 && hallwayEnd ==4) {//if 1 to 4
                    //we know we're on the left side because if we were on the right we'd do the old go to the elevator but don't trick
                    //turn out of room
                    if(sideHallStart == 1){
                        directions += "Turn Right"
                    }
                    else{//if side = 2 (any floors that have hallway 4 don't have any rooms in hallway 1 that are on side 3
                        directions += "Turn Left"
                    }
                    //forward, right, forward, left, forward
                    directions += "Move Forward"
                    directions += "Turn Right"
                    directions += "Move Forward"
                    directions += "Turn Left"
                    directions += "Move Forward"

                    //turn in to room
                    if(sideHallEnd == 1){
                        directions += "Turn Right"
                    }
                    else if (sideHallEnd == 2){
                        directions += "Turn Left"
                    }
                    //if they end on side 3 you don't have to do a final turn you just keep moving forward into the room
                }//end else if start hall 1 end hall 4
                else if (hallwayStart == 4 && hallwayEnd == 1){//if 4 to 1
                    //turn out of room (or don't if on side 3)
                    if (sideHallStart == 1){
                        directions += "Turn Left"
                    }
                    else if (sideHallStart == 2){
                        directions += "Turn Right"
                    }
                    if(start.number == "434" || start.number == "435" || start.number == "336" || start.number == "337" || start.number == "338" || start.number == "238") {//if you are rooms 434, 435, 336, 337, 338 or 238 then you forward right forward RIGHT forward
                        //all these rooms are to the right of hall 3 so it's a bit confusing
                        //forward right forward RIGHT forward
                        directions += "Move Forward"
                        directions += "Turn Right"
                        directions += "Move Forward"
                        directions += "Turn Right"
                        directions += "Move Forward"

                        //turn into room slightly differently
                        if (sideHallEnd == 1){
                            directions += "Turn Right"
                        }
                        else{//if end on side 2 (there is no side 3 on hall 1 where coming from the same floor as hall 4
                            directions += "Turn Left"
                        }
                    }

                    else{ //if not forward, right, forward, LEFT, forward
                        directions += "Move Forward"
                        directions += "Turn Right"
                        directions += "Move Forward"
                        directions += "Turn Left"
                        directions += "Move Forward"

                        //turn into room
                        if (sideHallEnd == 1){
                            directions += "Turn Left"
                        }
                        else{//if end on side 2 (there is no side 3 on hall 1 where coming from the same floor as hall 4
                            directions += "Turn Right"
                        }
                    }//end else if not those funky rooms
                }//end start in hall 4 end hall 1


                //we know that this is on the 2nd 3rd or 4th floors because the basement and 1st floor don't have hallway 4
                else if (hallwayStart == 3 && hallwayEnd == 4) {//if 3 to 4
                    var sideOf4: Boolean //true = right, false = left
                    if(start.number.equals("439") || start.number.equals("436")|| start.number.equals("339")|| start.number.equals("343")|| start.number.equals("340")|| start.number.equals("341")|| start.number.equals("240")|| start.number.equals("241")){
                        sideOf4 = false
                    }
                    else{
                        sideOf4 = true
                    }

                    if(sideHallStart == 1 && sideOf4 == true){
                        directions += "Turn Left"
                        directions += "Move Forward"
                        directions += "Turn Right"
                    }
                    else if(sideHallStart == 2 && sideOf4 == true){
                        directions += "Turn Right"
                        directions += "Move Forward"
                        directions += "Turn Right"
                    }
                    else if (sideHallStart == 1 && sideOf4 == false){
                        directions += "Turn Right"
                        directions += "Move Forward"
                        directions += "Turn Left"
                    }
                    else if (sideHallStart == 2 && sideOf4 == false){
                        directions += "Turn Left"
                        directions += "Move Forward"
                        directions += "Turn Left"
                    }

                    directions += "Move Forward"

                    //turn in to room
                    if(sideHallEnd == 1){
                        directions += "Turn Right"
                    }
                    else if (sideHallEnd == 2){
                        directions += "Turn Left"
                    }
                    //we don't need to do anything for side 3 because you don't need to turn you just go forward and are there
                }//end else if starting 3 and ending 4
                        //if 4 to 3
                //we know that this is on the 2nd 3rd or 4th floors because the basement and 1st floor don't have hallway 4
                else if (hallwayStart == 3 && hallwayEnd == 4) {//if 4 to 3
                    //turn out of room
                    if(sideHallEnd == 1){
                        directions += "Turn Left"
                    }
                    else if (sideHallEnd == 2){
                        directions += "Turn Right"
                    }
                    //we don't need to do anything for side 3 because you don't need to turn you just go forward and are there

                    directions += "Move Forward"

                    var sideOf4: Boolean //true = right, false = left
                    if(start.number.equals("439") || start.number.equals("436")|| start.number.equals("339")|| start.number.equals("343")|| start.number.equals("340")|| start.number.equals("341")|| start.number.equals("240")|| start.number.equals("241")){
                        sideOf4 = false
                    }
                    else{
                        sideOf4 = true
                    }

                    if(sideHallStart == 1 && sideOf4 == true){
                        directions += "Turn Left"
                        directions += "Move Forward"
                        directions += "Turn Right"
                    }
                    else if(sideHallStart == 2 && sideOf4 == true){
                        directions += "Turn Left"
                        directions += "Move Forward"
                        directions += "Turn Left"
                    }
                    else if (sideHallStart == 1 && sideOf4 == false){
                        directions += "Turn Right"
                        directions += "Move Forward"
                        directions += "Turn Left"
                    }
                    else if (sideHallStart == 2 && sideOf4 == false){
                        directions += "Turn Right"
                        directions += "Move Forward"
                        directions += "Turn Right"
                    }
                }//end else if starting 3 and ending 4

            }//end if we are on the same side of the building
        }//end if they're on the same floor.

        for(item in directions){
            Log.d("TAG", item)
        }


        val nextDirectionBtn: Button = findViewById(R.id.buttonNextStep)
        val directionText: TextView = findViewById(R.id.textViewMap)

        directionText.text = directions[0] //we have to update it to the first direction before displaying it
        var directionNum = 1 //normally we start indexing arrays at 0, but we already did 0 the line before so start counting at 1
        //display the map_view page


        nextDirectionBtn.setOnClickListener {
            if(directionNum < directions.size)
            {
                directionText.text = directions[directionNum]
                directionNum ++
            }
            else{
                reachDestination()
            }
        }//end on click listener

    }//end mapView Function


    fun startToElevator(directions: Array<String>): Array<String> {
        var tempDirections = directions //the directions array that's passed in is a val and can not be changed, but we need to change it
        //there is no traditional for loop in Kotlin so you have to kind finagle it using a for each style loop
        var i = start.directions.size -1
        for(direction in start.directions){
            var item = start.directions[i]

            if(item.equals("left")){
                tempDirections += "Turn left"
            }
            else if (item.equals("right")){
                tempDirections += "Turn right"
            }
            else {
                tempDirections += "Move Forward"
            }
            i--
        }
        return tempDirections
    }

    fun startToElevatorNoElevator(directions: Array<String>): Array<String> {
        var tempDirections = directions //the directions array that's passed in is a val and can not be changed, but we need to change it

        var modifiedStart = arrayOf<String>()
        var i = start.directions.size - 1
        while (i > 1) { //we want to stop at index 1 not including it
            modifiedStart += start.directions[i]
            i--
        }

        //there is no traditional for loop in Kotlin so you have to kind finagle it using a for each style loop
        var j = modifiedStart.size -1
        for(direction in modifiedStart){
            var item = modifiedStart[j]

            if(item.equals("left")){
                tempDirections += "Turn left"
            }
            else if (item.equals("right")){
                tempDirections += "Turn right"
            }
            else {
                tempDirections += "Move Forward"
            }
            i--
        }
        return tempDirections
    }

    fun endToElevator(directions: Array<String>): Array<String> {
        var tempDirections = directions //the directions array that's passed in is a val and can not be changed, but we need to change it


        for(item in end.directions){
            //because this is coming FROM the elevator, but the directions are for going TO the elevator we have to switch every left to right vice versa because you are facing the opposite direction
            if(item.equals("left")){
                tempDirections += "Turn right"
            }
            else if (item.equals("right")){
                tempDirections += "Turn left"
            }
            else {
                tempDirections += "Move Forward"
            }
        }
        return tempDirections
    }// end end to elevator

    fun endToElevatorNoElevator(directions: Array<String>): Array<String> {
        var tempDirections = directions //the directions array that's passed in is a val and can not be changed, but we need to change it


        var modifiedEnd = arrayOf<String>()
        var i = end.directions.size - 1
        while (i > 1) { //we want to stop at index 1 not including it
            modifiedEnd += end.directions[i]
            i--
        }

        for(item in modifiedEnd){
            //because this is coming FROM the elevator, but the directions are for going TO the elevator we have to switch every left to right vice versa because you are facing the opposite direction
            if(item.equals("left")){
                tempDirections += "Turn right"
            }
            else if (item.equals("right")){
                tempDirections += "Turn left"
            }
            else {
                tempDirections += "Move Forward"
            }
        }
        return tempDirections
    }// end end to elevator

    fun reachDestination(){
        setContentView(R.layout.reached_destination)

        val endBtn: Button = findViewById(R.id.buttonReturnHome)
        val arrived: TextView = findViewById(R.id.textViewConfirmation)

        arrived.text = "You've arrived at " + roomList.findRoomName(end)
        endBtn.setOnClickListener {
            start()
        }//end on click listener
    }

}//end class

