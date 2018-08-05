package building

import level.Level

class Building (
        private var levels: ArrayList<Level> = arrayListOf<Level>(),
        private var numberOfLevels: Int = 0

) {
    fun getLevels(): ArrayList<Level> {
        return this.levels
    }
    fun addLevel(level: Level): Boolean{
        if(this.levels.contains(level)){
            return false
        }
        this.levels.add(level)
        this.numberOfLevels += 1
        return true
    }
    fun deleteLevel(levelId: String): Boolean{
        if (searchLevelById(levelId)!=null){
            val level = searchLevelById(levelId)
            this.levels.remove(level)
            return true
        }
        return false
    }
    fun searchLevelById(levelId: String): Level?{
        val filteredLevels = levels.filter { it.getLevelId() == levelId }
        if (filteredLevels.count()>0){
            return filteredLevels[0]
        }
        return null
    }
}
