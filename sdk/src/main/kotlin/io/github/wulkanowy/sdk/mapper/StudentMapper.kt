package io.github.wulkanowy.sdk.mapper

import io.github.wulkanowy.sdk.pojo.StudentGender
import io.github.wulkanowy.sdk.pojo.StudentGuardian
import io.github.wulkanowy.sdk.pojo.StudentInfo
import io.github.wulkanowy.sdk.scrapper.toLocalDate
import io.github.wulkanowy.sdk.scrapper.student.StudentGuardian as ScrapperStudentGuardian
import io.github.wulkanowy.sdk.scrapper.student.StudentInfo as ScrapperStudentInfo

fun ScrapperStudentInfo.mapStudent() = StudentInfo(
    fullName = fullName,
    address = address,
    birthDate = birthDate.toLocalDate(),
    birthPlace = birthPlace,
    cellPhoneNumber = cellPhone.orEmpty(),
    correspondenceAddress = correspondenceAddress,
    email = email.orEmpty(),
    familyName = familyName.orEmpty(),
    firstName = name,
    gender = if (gender) StudentGender.MALE else StudentGender.FEMALE,
    parentsNames = motherAndFatherNames.orEmpty(),
    phoneNumber = homePhone.orEmpty(),
    hasPolishCitizenship = polishCitizenship == 1,
    registeredAddress = registeredAddress,
    secondName = middleName.orEmpty(),
    surname = lastName,
    guardians = listOfNotNull(
        guardianFirst?.toFamilyMember(),
        guardianSecond?.toFamilyMember()
    ),
    guardianFirst = guardianFirst?.toFamilyMember(),
    guardianSecond = guardianSecond?.toFamilyMember()
)

private fun ScrapperStudentGuardian.toFamilyMember() = StudentGuardian(
    fullName = fullName,
    email = email.orEmpty(),
    address = address,
    kinship = kinship.orEmpty(),
    phones = phone
)
