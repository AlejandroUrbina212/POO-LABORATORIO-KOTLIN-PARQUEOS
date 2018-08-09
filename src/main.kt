import building.Building
import car.Car
import level.Level
import parkingSpot.ParkingSpot
import wall.Wall
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.InputStream
private fun chooseAndGetFilePath(fileName: String): String {
    val dialog = FileDialog(null as Frame?, "Choose $fileName to open")
    dialog.mode = FileDialog.LOAD
    dialog.isVisible = true
    val file = (dialog.directory + dialog.file).replace("\\", "/")
    println("$file picked.")
    return file
}
private fun menus (numberOf: Int): String{
    var myMenu = ""
    when (numberOf){
        1 ->{
            myMenu=("""
        Welcome to the parking lot menu, please select a user profile:
        1. Administrator
        2. Driver
        3. exit

    """.trimIndent())
        }
        2 -> myMenu=("""
        Welcome to the parking lot administrator, please select the action you want to execute: :
        1. Create a new level.
        2. Delete a level.
        3. Show all levels
        4. exit
        """.trimIndent())
        3-> {
            myMenu=("""
                1. Add license Plate
                2. Exit
                """.trimIndent())
        }
    }
    return myMenu
    }

fun main(args: Array<String>) {
    val building = Building()
    val possibleParkingChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
    var row: Int = -1
    do{
        var wantsToContinue = true
        println("Please select an option from menu: ")
        print(menus(1))
        val selectedMenu1 = readLine()!!.toInt()
        when (selectedMenu1) {
            1 -> {
                do {
                    var wantsToContinueAdmin=true
                    println("Please select an option from menu:\n" + menus(2))
                    val optionAdmin = readLine()!!.toInt()

                    if (optionAdmin==1) {
                        println("Please enter the new level name.  ")
                        val levelName = readLine()!!
                        if (building.searchLevelByName(levelName)!=null){
                            println("Other level has already this name!")
                        } else {
                            println("Please enter the new level Id.")
                            val levelId = readLine()!!
                            if(building.searchLevelById(levelId)!=null){
                                println("This level is already registered!")
                            } else {
                                println("Please enter the color of the new level.")
                                val levelColor = readLine()!!
                                if (building.searchLevelByColor(levelColor)!=null){
                                    println("This color is already taken by a level")
                                } else{
                                    val newLevel = Level(name = levelName, levelId = levelId, color = levelColor, height = 0, width = 0)
                                    println("Please enter the root to your level file")
                                    val levelPath = chooseAndGetFilePath("Level")
                                    val inputStream: InputStream = File(levelPath).inputStream()
                                    val linesLength = arrayListOf<Int>()

                                    inputStream.bufferedReader().useLines {
                                        lines -> lines.forEach {
                                        row +=1
                                        println(it)
                                        linesLength.add(it.length)
                                        for (column in 0..(it.length-1)){
                                            val substring: String = it[column].toString()
                                            when (substring){
                                                "*" -> {val wall = Wall(positionX = column, positionY = row)
                                                    newLevel.addWall(wall)
                                                }
                                                else  -> if (possibleParkingChar.contains(substring)){
                                                    val parkingSpot = ParkingSpot(positionX = column, positionY = row, Symbol = substring, isEnabled = true)
                                                    newLevel.addParkingSpot(parkingSpot)
                                                }
                                            }
                                        }
                                    }}
                                    newLevel.setHeight(row+1)
                                    newLevel.setWidth(linesLength[0])
                                    if (building.verifyIfThereAreCharactersRepeated(newLevel)!=null){
                                        println("Your level has repeated characters in some parking-spots")
                                        println("Character / repeated x times")
                                        print("${building.verifyIfThereAreCharactersRepeated(newLevel)} \n")
                                    }
                                    for (i in 0..(linesLength.size-1)){
                                        if (linesLength[0]!=linesLength[i]){
                                            println("Cannot resolve level, inconsistency found in a row or column")
                                            newLevel.cantConstruct()
                                            break
                                        }
                                    }
                                    if (newLevel.getCanConstruct()){
                                        building.addLevel(level = newLevel)
                                        println("Level Constructed Successfully")
                                    }
                                }
                            }
                        }


                    }
                    else if (optionAdmin==2){
                        println("Please type the exact ID of the level you want to delete. (all characters supported) ")
                        val desiredDeletionLevelId = readLine()!!
                        val confirmedDelete: Boolean = building.deleteLevel(desiredDeletionLevelId)
                        if (confirmedDelete) println("Level with ID: $desiredDeletionLevelId deleted!")
                        else println("Level with ID: [$desiredDeletionLevelId] was not found!")
                    }
                    else if (optionAdmin==3){
                        for (level in building.getLevels()){
                            print("$level\n")
                        }
                    }
                    else if (optionAdmin==4) {
                        wantsToContinueAdmin = false
                    }

                } while (wantsToContinueAdmin)
            }
            2 -> {
                var wantsToContinueDriver = true

                if (building.verifyIfThereIsSpace()==null){
                    println("there is no space in this building")
                } else{
                    do {
                        println("Please select an option from menu: \n${menus(3)}")
                        val selection = readLine()?.toInt()
                        when (selection){
                            1->{
                                println("Please enter your license plate: ")
                                val licensePlate = readLine()!!
                                if (building.isCarInAnyLevel(licensePlate)==null){
                                    val availableLevels = building.levelsWithParkingSpotsAvailable()
                                    println("These levels are available: ")
                                    for (level in availableLevels){
                                        print(level)
                                    }
                                    println("Please insert the ID of the level you want to park your car: ")
                                    val levelSelected = readLine()!!
                                    if (building.searchLevelById(levelSelected)!=null){
                                        val levelToParkCar = building.searchLevelById(levelSelected)
                                        println("Please enter the letter or number of the parking spot where you want to park your car: ")
                                        val symbolOfParkingSpot = readLine()!!
                                        val parkingSpotOccupied = levelToParkCar!!.getParkingSpotBySymbol(symbolOfParkingSpot)
                                        if (parkingSpotOccupied!=null){
                                            val newCarPositionX = parkingSpotOccupied.getPositionX()
                                            val newCarPositionY = parkingSpotOccupied.getPositionY()
                                            val newCar = Car(
                                                    licensePlate = licensePlate,
                                                    positionX = newCarPositionX,
                                                    positionY = newCarPositionY
                                            )
                                            if (levelToParkCar.verifyCarInPosition(newCarPositionX, newCarPositionY)==null){
                                                levelToParkCar.addCar(newCar, parkingSpotOccupied)
                                                println("Your car is parked in parking Spot: ${parkingSpotOccupied.getSymbol()}" )
                                            }
                                            else{
                                                println("this space is already occupied by a car!")}
                                        } else {
                                            print("The parking spot selected doesn't exist or there is a car already occupying it\n")
                                        }
                                    } else {
                                        print("selected level does not exist!")}
                                } else {
                                    val levelWhereCarLocated = building.isCarInAnyLevel(licensePlate)
                                    if (levelWhereCarLocated != null) {
                                        println("Your car is located in level: \n$levelWhereCarLocated")
                                        println("Your car is in the parking spot with symbol ${levelWhereCarLocated.getCarParkingSpotSymbolByPlate(licensePlate)}")
                                    }
                                }
                            }
                            2 -> {
                                wantsToContinueDriver=false
                            }
                        }
                    }while (wantsToContinueDriver)
                }
            }
            3 -> wantsToContinue=false
        }
    } while (wantsToContinue)


}