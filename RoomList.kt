package com.zybooks.qumaps

import Room
import android.os.Bundle
import android.util.Log

class RoomList {

    var msg: String = "NO"
    var rooms = arrayOf<Room>() //this is an array of Room objects
    var roomNames = arrayOf<String>() //this is an array with the name of every room. We need this seperate from the rooms array so we can display it in the spinner widget
    lateinit var roomStart: Room //this is the room you're starting in.
    lateinit var roomEnd: Room //this is the room you're ending in.

    fun create() {
        fillRooms()
        fillRoomNames()
    }

    fun fillRoomNames()
    {
        for (item: Room in rooms){
            var name = ""
            if (item.number != "" && item.number != "417"){ //if the room has a number add the number to the name unless it is 417 or 418. 417 and 418 are the board room and board conference room and don't have real room numbers. they are only there for the app to know where they are.
                name += item.number + " "
            }
            if(item.altName != "") { //if the room has an alternate name add the alternate name to the name
                name += item.altName
            }
            roomNames += name //add the full name to the roomNames Array.
        }
    }

    fun findRoomObject(name: String): Room? {
        val space = name.indexOf(" ")
        if (space != -1) { //if there is a space (index of does not return -1)
            val num = name.substring(0, space)
            for (item: Room in rooms) {
                if (item.number == num) {
                    return item
                }
            }//end for
        }//end if space
        else {//if there is not a space so it doesn't have a room number (board room & board conference room)
            for (item: Room in rooms) {
                if (item.altName == name) {
                    return item
                }
            }//end for
        }//end else

        //in order to be able to call this function and assign the room to a variable in Main Activity, it HAS to return some room
        //and if you try to put in a room name that doesn't exist, it freaks out. However, we won't ever need this temp room
        //this function is ONLY called after you pick from the list of rooms. Any room you pick from that list will be on the list.
        //kotlin just freaks out, so there has to be a dummy room so it can calm down.
        var tempRoom = Room("0", "")
        return tempRoom
    }//end findRoomObject

    fun findRoomName(room: Room): String{
        var name = ""
        if (room.number != "" && room.number != "417" && !room.number.get(0).equals("B")){ //if the room has a number add the number to the name unless it is 417 or 418. 417 and 418 are the board room and board conference room and don't have real room numbers. they are only there for the app to know where they are.
            if(room.number.toInt() < 100){ //if its in the basement, put back the B-## part of the name that might've been taken out
                if(!room.number.get(0).equals("B")){ //if there isn't already the B at the beginning of the number
                    name+= "B-$room.number "  //ex. 33 -> B-33
                }
            }
            else { //if it ins't in the basement just add the number as it is
                name += room.number + " "
            }
        }
        else if (room.number.get(0).equals("B")){
            name += room.number + " "
        }
        if(room.altName != "") { //if the room has an alternate name add the alternate name to the name
            name += room.altName
        }
        return name
    }//end findRoomName function

     fun fillRooms() {
         //ok so we have to fill every room. because the rooms are sometimes weirdly number/named, we have to do this all manually
         //make a temp Room object and add it to the Rooms array. then use that same object to add the new one
         var tempRoom: Room

         tempRoom = Room("B-5", "Employee Lounge")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("B-10 ", "Mailroom")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("B-30", "Conference Room")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("B-33", "Business Office")
         tempRoom.setDirections("left", "forward", "", "","","","")
         rooms += tempRoom

         tempRoom = Room("B-37", "Advancement Office")
         tempRoom.setDirections("left", "forward", "left", "forward","right","","")
         rooms += tempRoom

         tempRoom = Room("B-38", "Human Resources/Payroll")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("104", "Student Financial Services")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("105", "Student Financial Services")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("108", "Director of Admissions Office Manager Office")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("109", "Admissions Counselors & Nursing Admissions Office")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("111", "Transfer Admissions & Admissions Marketing Office")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("114", "Campus Ministry Office")
         tempRoom.setDirections("right", "forward", "right", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("120", "Enrollment Office")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("121", "Student Development Office")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("122", "Assistant Vice President of Academic Affairs")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("124", "Academic Affairs Office")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("128", "President's Office")
         tempRoom.setDirections("left", "forward", "", "","","","")
         rooms += tempRoom

         tempRoom = Room("130", "Registrar's Office")
         tempRoom.setDirections("left", "forward", "left", "forward","right","","")
         rooms += tempRoom

         tempRoom = Room("134", "IT Services")
         tempRoom.setDirections("left", "forward", "left", "forward","right","","")
         rooms += tempRoom

         tempRoom = Room("135", "Graduate Studies Office")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("202", "")
         tempRoom.setDirections("right", "forward", "left", "forward","right","forward","left")
         rooms += tempRoom

         tempRoom = Room("205", "")
         tempRoom.setDirections("right", "forward", "left", "forward","right","forward","left")
         rooms += tempRoom

         tempRoom = Room("207", "")
         tempRoom.setDirections("right", "forward", "left", "forward","right","forward","right")
         rooms += tempRoom

         tempRoom = Room("208", "")
         tempRoom.setDirections("right", "forward", "left", "forward","right","forward","left")
         rooms += tempRoom

         tempRoom = Room("209", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("211", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("210", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("212", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("213", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("214", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("215", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("216", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("217", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("222", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("224", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("225", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("226", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("227", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("228", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("229", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("230", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("231", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("232", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("233", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("234", "")
         tempRoom.setDirections("left", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("238", "Student Lounge")
         tempRoom.setDirections("left", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("240", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","","")
         rooms += tempRoom

         tempRoom = Room("241", "Robotics Lab")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("243", "")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("244", "")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("245", "")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("246", "Cybersecurity Lab")
         tempRoom.setDirections("left", "forward", "left", "forward","right","","")
         rooms += tempRoom

         tempRoom = Room("249", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","forward","")
         rooms += tempRoom

         tempRoom = Room("250", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","forward","left")
         rooms += tempRoom

         tempRoom = Room("305", "")
         tempRoom.setDirections("right", "forward", "left", "forward","right","forward","right")
         rooms += tempRoom

         tempRoom = Room("306", "")
         tempRoom.setDirections("right", "forward", "left", "forward","right","forward","left")
         rooms += tempRoom

         tempRoom = Room("307", "ASL Lab")
         tempRoom.setDirections("right", "forward", "left", "forward","right","forward","left")
         rooms += tempRoom

         tempRoom = Room("309", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("310", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("311", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("312", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("313", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("314", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("315", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("316", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("317", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("320", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("321", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("322", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("323", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("325", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("326", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("328", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("330", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("332", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("334", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("336", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("337", "")
         tempRoom.setDirections("left", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("338", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("327", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("329", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("331", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("333", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("339", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("340", "")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("341", "")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("343", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","","")
         rooms += tempRoom

         tempRoom = Room("344", "")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("349", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","forward","left")
         rooms += tempRoom

         tempRoom = Room("350", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","forward","")
         rooms += tempRoom

         tempRoom = Room("351", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","forward","right")
         rooms += tempRoom

         tempRoom = Room("402", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("403", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("405", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("406", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("407", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("408", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("411", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("412", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("413", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("414", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("415", "")
         tempRoom.setDirections("right", "forward", "right", "","","","")
         rooms += tempRoom

         tempRoom = Room("416", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("419", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("422", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("423", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("424", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("425", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("426", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("427", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("428", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("429", "")
         tempRoom.setDirections("left", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("432", "")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("434", "Printing Lab")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("435", "Art Computer Lab")
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("436", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","","")
         rooms += tempRoom

         tempRoom = Room("439", "")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("441", "Drawing Studio")
         tempRoom.setDirections("left", "forward", "left", "forward","left","","")
         rooms += tempRoom

         tempRoom = Room("443", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","forward","left")
         rooms += tempRoom

         tempRoom = Room("445", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","forward","")
         rooms += tempRoom

         tempRoom = Room("447", "")
         tempRoom.setDirections("left", "forward", "left", "forward","right","","")
         rooms += tempRoom

         tempRoom = Room("417", "Conference Board Room") //I'm giving the number 417 to both conference board room and board room because they need a number to see where on the floor they are but it won't be displayed
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

         tempRoom = Room("417", "Board Room") //I'm giving the number 417 to both conference board room and board room because they need a number to see where on the floor they are but it won't be displayed
         tempRoom.setDirections("right", "forward", "left", "","","","")
         rooms += tempRoom

     }


}







