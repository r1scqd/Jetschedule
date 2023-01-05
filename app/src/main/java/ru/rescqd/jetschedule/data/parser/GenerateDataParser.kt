package ru.rescqd.jetschedule.data.parser

import ru.rescqd.jetschedule.data.container.GenerateDataContainer


object GenerateDataParser{
    sealed class RawDataType{
        object GROUP: RawDataType()
        object TEACHER: RawDataType()
    }
    private val subjectsPattern = Regex("colspan=\"3\" align=\"left\">(.+?)<")
    private val teachersPattern = Regex("colspan=\"4\" align=\"left\">(.+?)<")
    private val auditsPattern = Regex("<td align=\"left\">(.+?)</td></tr>")
    private val numSubjectPattern = Regex("rowspan=\"2\">([1-9])<")
    private val groupNamePattern = Regex("<b>(.+?)</b>")

    fun parse(html: String, rawDataType: RawDataType, identifity: String): List<GenerateDataContainer> {
        return when(rawDataType){
            is RawDataType.GROUP -> parseGroup(html, identifity)
            is RawDataType.TEACHER -> parseTeacher(html, identifity)
        }
    }

    private fun parseTeacher(html: String, teacher: String): List<GenerateDataContainer> {
        val subjectList = getSubjectList(html)
        val audienceList = getAudience(html)
        val pairOrderList = getPairOrder(html)
        val groupList = getTeacherList(html)
        val gdp = mutableListOf<GenerateDataContainer>()
        for (index in pairOrderList.indices){
            gdp.add(
                GenerateDataContainer(
                    subject = subjectList[index],
                    teacher = teacher,
                    audience = GenerateDataContainer.Audience(audienceList[index]),
                    pairOrder = pairOrderList[index],
                    group = groupList[index]
                )
            )
        }
        return gdp
    }


    private fun getSubjectList(html: String): List<String> = subjectsPattern.findAll(html).map { it.groupValues[1]}.toList()
    private fun getTeacherList(html: String): List<String> = teachersPattern.findAll(html).map { it.groupValues[1]}.toList()
    private fun getAudience(html: String): List<String> = auditsPattern.findAll(html).map { it.groupValues[1]}.toList()
    private fun getPairOrder(html: String): List<Int> = numSubjectPattern.findAll(html).map { it.groupValues[1].toInt()}.toList()

    private fun parseGroup(html: String, group: String): List<GenerateDataContainer> {

        val subjectList = getSubjectList(html)
        val audienceList = getAudience(html)
        val teacherList= getTeacherList(html)
        val pairOrderList = getPairOrder(html)
        val gdp = mutableListOf<GenerateDataContainer>()
        for (index in pairOrderList.indices){
            gdp.add(
                GenerateDataContainer(
                    subject = subjectList[index],
                    teacher = teacherList[index],
                    audience = GenerateDataContainer.Audience(audienceList[index]),
                    pairOrder = pairOrderList[index],
                    group = group
                )
            )
        }
        return gdp
    }




}