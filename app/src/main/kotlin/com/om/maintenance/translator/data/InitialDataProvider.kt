package com.om.maintenance.translator.data

import com.om.maintenance.translator.data.entity.MaintenanceTerm

object InitialDataProvider {
    fun getInitialTerms(): List<MaintenanceTerm> {
        return listOf(
            // الكهربائيات
            MaintenanceTerm(arabicTerm = "إصلاح مفتاح المكيف", englishTerm = "REPAIR OF AIR CON SWITCH", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح سخان المياه", englishTerm = "REPAIR OF WATER HEATER", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح سخان الطعام", englishTerm = "REPAIR OF FOOD WARMER", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح مفتاح اللمبة", englishTerm = "REPAIR OF LAMP SWITCH", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح مروحة الشفط / سقف", englishTerm = "REPAIR OF EXHAUST FAN / CEILING FAN", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح مقبس كهربائي (المأخذ)", englishTerm = "REPAIR OF OUTLET", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "استبدال كشاف الإضاءة", englishTerm = "REPLACE OF FLOOD LIGHT", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إنارة الشوارع العالية", englishTerm = "HIGH TIENTSIN STREET LIGHT", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إزالة اللوح الخشبي", englishTerm = "TAKE DOWN WOODEN BOARD", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح الشفاط", englishTerm = "REPAIR OF EXTRACT FAN", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "تغي��ر اللمبات", englishTerm = "REPLACE OF LAMPS", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "نجارة تحتاج", englishTerm = "NEED CARPENTRY TO RELOCATE OF OFFICE FURNITURE", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح تهرب المكيف", englishTerm = "REPAIR OF AIR CON LEAKING", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح المكيف", englishTerm = "REPAIR OF AIR CON", category = "كهربائي")
        )
    }
}
