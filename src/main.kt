import building.Building
import com.sun.org.apache.xpath.internal.operations.Bool
import level.Level
import parkingSpot.ParkingSpot
import wall.Wall
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.InputStream
import java.util.concurrent.atomic.AtomicInteger

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
        Welcome to the parking lot administrator, please select a user profile:
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
    var wantsToContinue = true
    val possibleParkingChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
    print(possibleParkingChar)
    println("Please select an option from menu: ")
    print(menus(1))
    val selectedMenu1 = readLine()!!.toInt()
    var row: Int = -1
    do{
        when (selectedMenu1) {
            1 -> {
                var wantsToContinueAdmin = false
                do {
                    println("Please select an option from menu:\n" + menus(2))
                    val optionAdmin = readLine()!!.toInt()
                    when (optionAdmin) {
                        1-> {
                            println("Please enter the new level name.  ")
                            val levelName = readLine()!!
                            println("Please enter the new level Id.")
                            val levelId = readLine()!!
                            if(building.searchLevelById(levelId)!=null){
                                println("This level is already registered!")
                                wantsToContinueAdmin=false
                            } else {
                                println("Please enter the color of the new level.")
                                val levelColor = readLine()!!
                                val newLevel = Level(
                                        name = levelName,
                                        levelId = levelId,
                                        color = levelColor,
                                        height = 0,
                                        width = 0
                                )
                                println("Please enter the root to your map file")
                                val levelPath = chooseAndGetFilePath("Level")
                                val inputStream: InputStream = File(levelPath).inputStream()
                                var linesLength = arrayListOf<Int>()

                                inputStream.bufferedReader().useLines {

                                    lines -> lines.forEach {
                                    row +=1
                                    println("$it")
                                    linesLength.add(it.length)

                                    for (column in 0..(it.length-1)){
                                        var substring: String = it[column].toString()
                                        when (substring){
                                            "*" -> {val wall = Wall(
                                                    positionX = column,
                                                    positionY = row
                                            )
                                                newLevel.addWall(wall)
                                            }
                                            else  -> if (possibleParkingChar.contains(substring)){

                                                val parkingSpot = ParkingSpot(
                                                        positionX = column,
                                                        positionY = row,
                                                        Symbol = substring
                                                )
                                                newLevel.addParkingSpot(parkingSpot)
                                            }
                                        }
                                    }

                                }

                                }

                                for (i in 0..(linesLength.size-1)){
                                    if (linesLength[0]!=linesLength[i]){
                                        println("Cannot resolve level, inconsistency found in a row or column")
                                        wantsToContinueAdmin=false

                                    }
                                }
                                newLevel.setHeight(row+1)
                                newLevel.setWidth(linesLength[0])
                                building.addLevel(newLevel)


                                val filteredSymbols = arrayListOf<String>()
                                newLevel.getParkingSpots().forEach { x -> filteredSymbols.add(x.getSymbol())}
                                val groupedChars = (filteredSymbols.groupingBy { it }.eachCount().filter { it.value > 1 })
                                if (groupedChars.count()>0){
                                    println("""Cannot resolve level, there are some parking spots that contain the same Symbol identifier as shown:
[Identifier of the parking spot: repeated x times]:
$groupedChars.
                                """.trimMargin())
                                    wantsToContinueAdmin=false
                                }
                                else {
                                    println("Level was created successfully!")
                                }
                            }

                        }
                        2->{
                            println("Please type the exact name of the level you want to delete. (all characters supported) ")
                            val desiredDeletionLevelId = readLine()!!
                            var confirmedDelete: Boolean = building.deleteLevel(desiredDeletionLevelId)
                            if (confirmedDelete) println("Level with ID: $desiredDeletionLevelId deleted!")
                            else println("Level with ID: $desiredDeletionLevelId wan not found!")
                        }
                        3->{
                            for (level in building.getLevels()){
                                print(level)
                            }

                        }
                        4->wantsToContinueAdmin=false
                    }

                } while (wantsToContinueAdmin)
            }
            2 -> {

            }
            3 -> wantsToContinue = false

        }



    } while (wantsToContinue)

}