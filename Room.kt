import android.hardware.biometrics.BiometricManager.Strings
import android.os.Bundle

class Room constructor(var number: String, val altName: String) {


    //array of strings for directions to elevator, print in reverse for instructions from elevator to class.
    var directions = arrayOf<String>()

    fun setDirections(direction1: String, direction2: String, direction3: String, direction4: String, direction5: String, direction6: String, direction7: String, ){
        //I can't figure out how to send an array as an argument, so we're just going to pass all the parts and store it in here as an array
        if(direction1 != "") {
            directions += direction1
        }
        if(direction2 != "") {
            directions += direction2
        }
        if(direction3 != "") {
            directions += direction3
        }

        if(direction4 != "") {
            directions += direction4
        }

        if(direction5 != "") {
            directions += direction5
        }

        if(direction6 != "") {
            directions += direction6
        }

        if(direction7 != "") {
            directions += direction7
        }

    }//end setDirections
}//end room