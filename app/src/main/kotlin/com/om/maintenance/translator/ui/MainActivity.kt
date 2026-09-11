package com.om.maintenance.translator.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.om.maintenance.translator.R
import com.om.maintenance.translator.data.database.MaintenanceDatabase
import com.om.maintenance.translator.data.entity.MaintenanceTerm
import com.om.maintenance.translator.data.repository.MaintenanceRepository
import com.om.maintenance.translator.databinding.ActivityMainBinding
import com.om.maintenance.translator.ui.adapter.MaintenanceAdapter
import com.om.maintenance.translator.ui.viewmodel.MaintenanceViewModel
import com.om.maintenance.translator.ui.viewmodel.MaintenanceViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MaintenanceViewModel by viewModels {
        val database = MaintenanceDatabase.getDatabase(this)
        val repository = MaintenanceRepository(database.maintenanceTermDao())
        MaintenanceViewModelFactory(repository)
    }
    
    private lateinit var adapter: MaintenanceAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        setupDatabase()
        observeData()
    }
    
    private fun setupUI() {
        // Setup RecyclerView
        adapter = MaintenanceAdapter(
            onDeleteClick = { term -> deleteTermDialog(term) },
            onEditClick = { term -> editTerm(term) }
        )
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
        
        // Add Button
        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddTermActivity::class.java))
        }
        
        // Search EditText
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    viewModel.allTerms.observe(this@MainActivity) { terms ->
                        updateUI(terms)
                    }
                } else {
                    viewModel.searchTerms(query).observe(this@MainActivity) { results ->
                        updateUI(results)
                    }
                }
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun setupDatabase() {
        lifecycleScope.launch {
            val database = MaintenanceDatabase.getDatabase(this@MainActivity)
            val dao = database.maintenanceTermDao()
            val count = dao.getTermsCount()
            
            if (count == 0) {
                // Insert initial data from the images
                val initialTerms = getInitialTerms()
                initialTerms.forEach { term ->
                    dao.insertTerm(term)
                }
            }
        }
    }
    
    private fun observeData() {
        viewModel.allTerms.observe(this) { terms ->
            updateUI(terms)
        }
    }
    
    private fun updateUI(terms: List<MaintenanceTerm>) {
        if (terms.isEmpty()) {
            binding.emptyStateView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyStateView.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            adapter.submitList(terms)
            binding.totalCountTv.text = getString(R.string.total_terms, terms.size)
        }
    }
    
    private fun deleteTermDialog(term: MaintenanceTerm) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("حذف المصطلح")
        builder.setMessage("هل تريد حذف: ${term.arabicTerm}؟")
        builder.setPositiveButton("حذف") { _, _ ->
            viewModel.deleteTerm(term)
        }
        builder.setNegativeButton("إلغاء", null)
        builder.show()
    }
    
    private fun editTerm(term: MaintenanceTerm) {
        val intent = Intent(this, AddTermActivity::class.java)
        intent.putExtra("term_id", term.id)
        startActivity(intent)
    }
    
    private fun getInitialTerms(): List<MaintenanceTerm> {
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
            MaintenanceTerm(arabicTerm = "تغيير اللمبات", englishTerm = "REPLACE OF LAMPS", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "معالجة تذبذب الكهرباء", englishTerm = "RECTIFY POWER FLUCTUATION", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "تركيب 10 مقبس كهربائي", englishTerm = "INSTALLATION OF 10 EA. ELECT. OUTLETS", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح البوابة الإلكترونية", englishTerm = "REPAIR OF BARRIER GATE", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "اختبار أحمال الونش", englishTerm = "LOAD TEST OF HOIST", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "معالجة تسرب ماء السقف", englishTerm = "RECTIFY WATER LEAKING FROM CEILING", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "معالجة تسرب ماء الجدار", englishTerm = "RECTIFY OF WATER LEAKING FROM THE WALL", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح تسرب / انقطاع ماء البرادة", englishTerm = "REPAIR NO WATER IN WATER COOLER LEAKING", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح قاطع الكهرباء", englishTerm = "RECTIFY OF CIRCUIT BREAKER", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "معالجة التماس كهربائي", englishTerm = "RECTIFY ELECTRICAL SHORT CIRCUIT", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "توفير توصيلتين كهربائيتين", englishTerm = "PROVIDE OF 2EACH EXTENSION", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "استبدال لمبات المدخل", englishTerm = "REPLACE OF LAMPS AT ENTRANCE", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح مصيدة الناموس", englishTerm = "REPAIR OF INSECT KILLER LIGHT", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح جرس الباب", englishTerm = "REPAIR OF DOOR BELL", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "استبدال لمبات الممر", englishTerm = "REPLACE OF LAMPS AT CORRIDOR", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "توصيل كهرباء السخان", englishTerm = "CONNECT POWER SPLAY FOR WATER HEATER", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "معالجة انقطاع الكهرباء", englishTerm = "RECTIFY NO POWER SUPPLY", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "تركيب مجاري أسلاك (ديكور)", englishTerm = "TRUNKING OF ELECT'L WIRE", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح لمبات الممر", englishTerm = "REPAIR OF LAMPS AT CORRIDOR", category = "كهربائي"),
            MaintenanceTerm(arabicTerm = "إصلاح كشاف برج الحراسة 3", englishTerm = "REPAIR OF FLOOD LIGHT AT SECURITY GUARD TOWER NO.03", category = "كهربائي"),
            
            // السباكة
            MaintenanceTerm(arabicTerm = "استبدال ساعة الدش", englishTerm = "REPLACE OF HAND SHOWER", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "استبدال فلتر المياه", englishTerm = "REPLACE WATER FILTER", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "تنظيف خزان المياه", englishTerm = "CLEANING OF WATER TANK", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "استبدال خلاط مغسلة اليدين", englishTerm = "REPLACE OF WASH BASIN MIXING FAUCET", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "استبدال خلاط مجلى المطبخ", englishTerm = "REPLACE OF KITCHEN SINK MIXING FAUCET", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "استبدال خلاط البانيو", englishTerm = "REPLACE OF BATH TUB MIXING FAUCET", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "استبدال محبس زاوية", englishTerm = "REPLACE OF ANGEL VALVE", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "استبدال لي ماء / سيفون تصريف", englishTerm = "REPLACE OF WATER HOSE, P-TRAP", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "إصلاح تهرب السيفون", englishTerm = "REPAIR OF FLUSH TANK LEAKING", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "معالجة تهرب ماسورة مياه", englishTerm = "RECTIFY OF WATER PIPE LEAKING", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "معالجة كسر ماسورة مياه", englishTerm = "RECTIFY OF WATER PIPE BROKEN", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "إصلاح تهرب ساعة الدش", englishTerm = "REPAIR OF HAND SHOWER LEAKING", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "إصلاح سخان المياه", englishTerm = "REPAIR OF WATER HEATER", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "شفط البيارة / خزان الصرف", englishTerm = "SUCK-UP OF MANHOLE/WASTE TANK", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "إصلاح صندوق الطرد (السيفون)", englishTerm = "REPAIR OF FLUSH TANK", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "تسليك استسداد الكرسي (WC)", englishTerm = "REPAIR OF WC BLOCKED", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "توفير مواد سباكة لصيانة الموقع", englishTerm = "PROVIDE PLUMBING MATERIALS TO MAINTAIN SITE", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "تسليك استسداد المجاري والبيارات", englishTerm = "RECTIFY OF DRAIN SEWAGE AND MANHOLE CLOGGED UP", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "فحص خط المياه الداخلي (تغير اللون/الانسداج)", englishTerm = "CHECK OF INTERNAL WATER SUPPLY LINE...", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "إصلاح تهرب ماسورة نحاس", englishTerm = "REPAIR OF LEAKING COPPER PIPE", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "معالجة نظام الدعم المالي (القطاع الغذائية)", englishTerm = "RECTIFY OF WATER BACK UP SYSTEM NO WATER SUPPLY", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "معالجة انقطاع المياه", englishTerm = "RECTIFY NO WATER SUPPLY", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "إصلاح تهرب ماسورة عداد المياه", englishTerm = "REPAIR OF WATER METER PIPE LEAKING", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "تعبئة غاز الكسجين والأسيتيلين بالأسطوانات", englishTerm = "REFILL OXYGEN & ACETYLENE GAS IN CYLINDERS", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "إصلاح تهرب كبير بمضخة الوقود", englishTerm = "REPAIR OF FUEL DISPENSER LEAKING TOO MUCH", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "تسليك استسداد مجلى المطبخ", englishTerm = "RECTIFY OF KITCHEN SINK BLOCKED", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "تسليك استسداد البانيو", englishTerm = "BATH TUB CLOGGED UP", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "معالجة استسداد خط الصرف", englishTerm = "RECTIFY OF DRAIN LINE CLOGGED-UP", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "دهان نقطة تجمع النفايات", englishTerm = "PAINTING FOR GARBAGE POINT", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "معالجة انقطاع المياه عن نصف المبنى", englishTerm = "RECTIFY NO WATER SUPPLY AT HALF OF THE BULDG", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "معالجة الروائح الكريهة في الحمام", englishTerm = "RECTIFY BAD SMELL FROM TOILET", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "معالجة كسر ماسورة وتسرق مياه غزيرة", englishTerm = "RECTIFY WATER PIPE BROKEN, WATER FLOODING TOO MUCH", category = "سباكة"),
            MaintenanceTerm(arabicTerm = "إصلاح حنفية الحديقة المكسورة", englishTerm = "REPAIR OF GARDEN FAUCET BROKEN", category = "سباكة"),
            
            // النجارة
            MaintenanceTerm(arabicTerm = "إصلاح قفل باب خشبي", englishTerm = "REPAIR OF WOODEN DOOR LOCK", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح قفل باب حديد", englishTerm = "REPAIR OF STEEL DOOR DOOR LOCK", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح باب ألومنيوم", englishTerm = "REPAIR OF ALUMINUM DOOR", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "استبدال ألواح السقف المستعار", englishTerm = "REPLACE OF CEILING BOARD", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح دولاب ألومنيوم", englishTerm = "REPAIR OF ALUMINUM CABINET", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح دولاب خشبي", englishTerm = "REPAIR OF WOODEN CABINET", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح طاولة خشبية", englishTerm = "REPAIR OF WOODEN TABLE.", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "تعليق لوحة إرشادية", englishTerm = "HUNG UP SIGN BOARD", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح السقف المستعار", englishTerm = "REPAIR OF CILLING BOARD", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "نقل لوحة إرشادية", englishTerm = "RELOCATE SIGN BOARD", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إزالة النفايات / المخلفات", englishTerm = "REMOVE OF GARBAGE", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح باب خشبي (غرفة 126)", englishTerm = "REPAIR OF WOODEN DOOR AT ROOM NO.126", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "غسيل وشامبو سجاد وكنب", englishTerm = "SHOOMPING CARPET &SOFA", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "غسيل سجاد بالشامبو (غرفة 106)", englishTerm = "SHAMPOOINING OF CARPET AT ROOM NO.106", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "تعليق شاشة تلفزيون", englishTerm = "HANG UP TV SCREEN", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إزالة مخلفات / مواد غير مرغوبة", englishTerm = "REMOVE UNWANTED MATERIALS", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إزالة لوح خشبي", englishTerm = "TAKE DOWN WOODEN BOARD", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "تعليق لوحة إرشادية في الغرفة", englishTerm = "HANG UP SING BOARD AT ROOM", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "قص الحشائش (منطقة ألفا-24)", englishTerm = "CUTTING OF GRASS AT ALFA AREA-24", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح دولاب خشبي", englishTerm = "REPAIR OF WOODEN CABINET", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح مزالج الباب (قفل الدراجس)", englishTerm = "FIX OF DOOR LATCH", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "استبدال رافعة (ونش)", englishTerm = "REPLACE OF HOIST.", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "استبدال صندوق الإسعافات الأولية", englishTerm = "REPLACE OF FIRST AID BOX", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "توفير عمال لإعادة ترتيب أثاث الفصول", englishTerm = "NEED LABORS TO REARRANGE CLASS ROOM FURNITURE.", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح سياج / شبك حديدي", englishTerm = "REPAIR OF METAL FENCE", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "تعليق جهاز بروجيكتور", englishTerm = "HANG UP EROJECTOR", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح نافذة ألومنيوم", englishTerm = "REPAIR OF ALUMINUM WINDOW.", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "تعليق لوحة إرشادية", englishTerm = "HANG UP SIGN BOARD", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إعادة تعيين رمز الفقفل الرقمي للباب", englishTerm = "RESET PASSWORD FOR DIGITAL DOOR LOCK", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح كرسي مكتب", englishTerm = "REPAIR OF OFFICE CHAIR", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح ستارة / سكة ستارة", englishTerm = "REPAIR OF CURTAIN RAILING", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح حمايات / شباك نافذة حديد", englishTerm = "REPAIR OF STEEL WINDOW GRILLE.", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "وضع سيليكون حول النافذة", englishTerm = "APPLY SILICONE AROUND WINDOW", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح كرسي", englishTerm = "REPAIR OF CHAIR", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح ستارة", englishTerm = "REPAIR OF CURTAIN.", category = "نجارة"),
            MaintenanceTerm(arabicTerm = "إصلاح شيك / قاطع الجبس في منطقة الاسترخاء", englishTerm = "FIX THE GIRL FOR GYPSUM PROD AT RESORPTION ARIA", category = "نجارة")
        )
    }
}
