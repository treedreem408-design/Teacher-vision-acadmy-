package com.example.data

import com.example.model.AcademyCourse
import com.example.model.AppLanguage
import com.example.model.CourseCategory
import com.example.model.DocumentPage
import com.example.model.LeaderboardUser
import com.example.model.LiveBannerUpdate
import com.example.model.LiveClassItem
import com.example.model.Quiz
import com.example.model.QuizAttemptResult
import com.example.model.QuizQuestion
import com.example.model.StudentEnrollment
import com.example.model.StudyDocument
import com.example.model.StudyFolder
import com.example.model.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object AcademyRepository {

    private val _userSession = MutableStateFlow(
        UserSession(
            mobile = "7888213000",
            name = "Academy Aspirant",
            selectedCourse = CourseCategory.UGC_NET,
            isAdmin = true,
            isLoggedIn = true
        )
    )
    val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun login(mobile: String, name: String, course: CourseCategory) {
        val isAdminUser = (mobile.trim() == "7888213000")
        _userSession.value = UserSession(
            mobile = mobile,
            name = if (name.isBlank()) (if (isAdminUser) "Admin (Owner)" else "Student") else name,
            selectedCourse = course,
            isAdmin = isAdminUser,
            isLoggedIn = true
        )
    }

    fun logout() {
        _userSession.value = UserSession(isLoggedIn = false)
    }

    fun selectCourse(category: CourseCategory) {
        _userSession.value = _userSession.value.copy(selectedCourse = category)
    }

    // --- Live Banner Updates ---
    private val _liveUpdates = MutableStateFlow(
        listOf(
            LiveBannerUpdate(
                id = "up_1",
                title = "UGC NET 2026: Online Form Submission Started",
                titlePa = "ਯੂ.ਜੀ.ਸੀ. ਨੈੱਟ 2026: ਆਨਲਾਈਨ ਫਾਰਮ ਭਰਨੇ ਸ਼ੁਰੂ",
                titleHi = "यूजीसी नेट 2026: ऑनलाइन फॉर्म शुरू",
                subtitle = "Exam dates announced. Prepare Paper 1 & Paper 2 with Teacher Vision Academy.",
                tag = "EXAM ALERT",
                date = "Latest Update",
                isLiveNow = true,
                linkUrl = "https://ugcnet.nta.nic.in"
            ),
            LiveBannerUpdate(
                id = "up_2",
                title = "Punjab Master Cadre (Punjabi) 2026 Batch",
                titlePa = "ਪੰਜਾਬ ਮਾਸਟਰ ਕਾਡਰ (ਪੰਜਾਬੀ) 2026 ਵਿਸ਼ੇਸ਼ ਬੈਚ",
                titleHi = "पंजाब मास्टर काडर (पंजाबी) 2026 विशेष बैच",
                subtitle = "Complete Sahit, Viyakaran, and Pedagogy notes with test series.",
                tag = "NEW BATCH",
                date = "Live Daily 7 PM",
                isLiveNow = true,
                linkUrl = "https://youtube.com/live/teachervision"
            ),
            LiveBannerUpdate(
                id = "up_3",
                title = "B.Ed Semester Pedagogy Notes & PYQs Uploaded",
                titlePa = "ਬੀ.ਐੱਡ. ਸਮੈਸਟਰ ਨੋਟਸ ਅਤੇ ਪਿਛਲੇ ਸਾਲਾਂ ਦੇ ਪ੍ਰਸ਼ਨ ਪੱਤਰ",
                titleHi = "बी.एड सेमेस्टर नोट्स और पिछले वर्षों के प्रश्न",
                subtitle = "Unit-wise solved questions for Punjabi & English mediums.",
                tag = "NOTES UPLOAD",
                date = "Today",
                isLiveNow = false
            ),
            LiveBannerUpdate(
                id = "up_4",
                title = "PSTET 2026 CDP Rapid Fire Live Mock Test",
                titlePa = "ਪੀ.ਐੱਸ.ਟੈੱਟ 2026 ਬਾਲ ਵਿਕਾਸ ਲਾਈਵ ਮੌਕ ਟੈਸਟ",
                titleHi = "पीटेट 2026 बाल विकास लाइव टेस्ट",
                subtitle = "Check your all-Punjab ranking with negative marking calculation.",
                tag = "TEST SERIES",
                date = "Active Now",
                isLiveNow = true
            )
        )
    )
    val liveUpdates: StateFlow<List<LiveBannerUpdate>> = _liveUpdates.asStateFlow()

    fun addLiveUpdate(title: String, titlePa: String, subtitle: String, tag: String) {
        val newItem = LiveBannerUpdate(
            id = UUID.randomUUID().toString(),
            title = title,
            titlePa = if (titlePa.isBlank()) title else titlePa,
            titleHi = title,
            subtitle = subtitle,
            tag = tag,
            date = "Just now",
            isLiveNow = true
        )
        _liveUpdates.value = listOf(newItem) + _liveUpdates.value
    }

    // --- Courses List ---
    private val _courses = MutableStateFlow(
        listOf(
            AcademyCourse(
                id = "c_ugc_net",
                title = "UGC NET Paper 1 + Punjabi (Paper 2) Master Batch",
                category = CourseCategory.UGC_NET,
                description = "Comprehensive master preparation for NTA UGC NET / JRF with 10 units of Paper 1 and Punjabi Sahit.",
                instructor = "Dr. Manpreet Singh (NET-JRF)",
                fee = 4999,
                originalFee = 9999,
                totalLectures = 140,
                totalNotes = 85,
                totalTests = 45,
                isEnrolled = true
            ),
            AcademyCourse(
                id = "c_master_cadre",
                title = "Punjab Master Cadre Punjabi Special Selection Batch",
                category = CourseCategory.PUNJAB_GOVT,
                description = "Complete syllabus coverage: Gurmat Kavya, Sufi Kavya, Qissa, Modern Poetry, Punjabi Grammar, and History.",
                instructor = "Prof. Jaswinder Kaur",
                fee = 3499,
                originalFee = 7000,
                totalLectures = 95,
                totalNotes = 60,
                totalTests = 30,
                isEnrolled = true
            ),
            AcademyCourse(
                id = "c_bed_pedagogy",
                title = "B.Ed Foundation & Teaching Pedagogy All Semesters",
                category = CourseCategory.B_ED,
                description = "Childhood, Contemporary India, Philosophical foundations, and Pedagogy of Teaching subjects.",
                instructor = "TVA Academic Board",
                fee = 1999,
                originalFee = 4500,
                totalLectures = 50,
                totalNotes = 40,
                totalTests = 15,
                isEnrolled = false
            ),
            AcademyCourse(
                id = "c_ma_punjabi",
                title = "MA Punjabi Semester 1-4 Complete Solved Guide",
                category = CourseCategory.MA,
                description = "Punjab University, Punjabi University, GNDU syllabus wise detailed thesis and text explanations.",
                instructor = "Dr. H. S. Dhillon",
                fee = 2499,
                originalFee = 5500,
                totalLectures = 65,
                totalNotes = 55,
                totalTests = 20,
                isEnrolled = false
            ),
            AcademyCourse(
                id = "c_pstet",
                title = "PSTET 2026 Target Score 120+ Batch",
                category = CourseCategory.PUNJAB_GOVT,
                description = "Child Development and Pedagogy, Punjabi, English, Mathematics, and Social Science / EVS.",
                instructor = "Teacher Vision Faculty Team",
                fee = 2999,
                originalFee = 6000,
                totalLectures = 80,
                totalNotes = 50,
                totalTests = 25,
                isEnrolled = false
            )
        )
    )
    val courses: StateFlow<List<AcademyCourse>> = _courses.asStateFlow()

    // --- Study Folders ---
    private val _folders = MutableStateFlow(
        listOf(
            // UGC NET Folders
            StudyFolder("f_ugc_p1", "Paper 1 (General Teaching & Research)", "ਪੇਪਰ 1 (ਅਧਿਆਪਨ ਤੇ ਖੋਜ ਰੁਚੀ)", CourseCategory.UGC_NET, null, 10),
            StudyFolder("f_ugc_p1_u1", "Unit 1: Teaching Aptitude", "ਯੂਨਿਟ 1: ਅਧਿਆਪਨ ਰੁਚੀ", CourseCategory.UGC_NET, "f_ugc_p1", 4),
            StudyFolder("f_ugc_p1_u2", "Unit 2: Research Aptitude", "ਯੂਨਿਟ 2: ਖੋਜ ਕਾਰਜ ਵਿਧੀ", CourseCategory.UGC_NET, "f_ugc_p1", 3),
            StudyFolder("f_ugc_p1_u4", "Unit 4: Communication", "ਯੂਨਿਟ 4: ਸੰਚਾਰ ਪ੍ਰਣਾਲੀ", CourseCategory.UGC_NET, "f_ugc_p1", 2),
            StudyFolder("f_ugc_p1_u8", "Unit 8: ICT (Digital Higher Education)", "ਯੂਨਿਟ 8: ਆਈ.ਸੀ.ਟੀ.", CourseCategory.UGC_NET, "f_ugc_p1", 2),
            StudyFolder("f_ugc_p2_punjabi", "Paper 2: Punjabi Sahit (ਯੂ.ਜੀ.ਸੀ. ਨੈੱਟ ਪੰਜਾਬੀ)", "ਪੇਪਰ 2: ਪੰਜਾਬੀ ਸਾਹਿਤ", CourseCategory.UGC_NET, null, 8),
            
            // MA Punjabi Folders
            StudyFolder("f_ma_gurmat", "ਗੁਰਮਤਿ ਕਾਵਿ ਧਾਰਾ (Gurmat Kavya)", "ਗੁਰਮਤਿ ਕਾਵਿ ਧਾਰਾ", CourseCategory.MA, null, 5),
            StudyFolder("f_ma_sufi", "ਸੂਫ਼ੀ ਕਾਵਿ ਅਤੇ ਕਿੱਸਾ ਕਾਵਿ", "ਸੂਫ਼ੀ ਤੇ ਕਿੱਸਾ ਕਾਵਿ", CourseCategory.MA, null, 4),
            StudyFolder("f_ma_natak", "ਆਧੁਨਿਕ ਪੰਜਾਬੀ ਨਾਟਕ ਤੇ ਇਕਾਂਗੀ", "ਪੰਜਾਬੀ ਨਾਟਕ", CourseCategory.MA, null, 3),

            // B.Ed Folders
            StudyFolder("f_bed_childhood", "Childhood and Growing Up", "ਬਾਲ ਵਿਕਾਸ ਅਤੇ ਵਾਧਾ", CourseCategory.B_ED, null, 4),
            StudyFolder("f_bed_contemporary", "Contemporary India & Education", "ਸਮਕਾਲੀ ਭਾਰਤ ਅਤੇ ਸਿੱਖਿਆ", CourseCategory.B_ED, null, 3),
            StudyFolder("f_bed_pedagogy_lang", "Pedagogy of Punjabi Language", "ਪੰਜਾਬੀ ਭਾਸ਼ਾ ਅਧਿਆਪਨ ਵਿਧੀ", CourseCategory.B_ED, null, 4),

            // Punjab Govt Folders
            StudyFolder("f_master_cadre_notes", "Punjab Master Cadre Punjabi Special", "ਮਾਸਟਰ ਕਾਡਰ ਪੰਜਾਬੀ ਨੋਟਸ", CourseCategory.PUNJAB_GOVT, null, 7),
            StudyFolder("f_pstet_cdp", "PSTET Child Development & Pedagogy", "ਪੀ.ਐੱਸ.ਟੈੱਟ ਬਾਲ ਮਨੋਵਿਗਿਆਨ", CourseCategory.PUNJAB_GOVT, null, 5),
            StudyFolder("f_punjab_gk", "Punjab History, Culture & GK 500 MCQs", "ਪੰਜਾਬ ਜੀ.ਕੇ. ਤੇ ਇਤਿਹਾਸ", CourseCategory.PUNJAB_GOVT, null, 3)
        )
    )
    val folders: StateFlow<List<StudyFolder>> = _folders.asStateFlow()

    // --- Study Documents (PDFs rendered with in-app viewer & anti-piracy) ---
    private val _documents = MutableStateFlow(
        listOf(
            StudyDocument(
                id = "doc_ugc_u1_notes",
                folderId = "f_ugc_p1_u1",
                courseCategory = CourseCategory.UGC_NET,
                title = "UGC NET Unit 1: Teaching Aptitude Master Revision Notes",
                titlePa = "ਯੂਨਿਟ 1: ਅਧਿਆਪਨ ਰੁਚੀ - ਸੰਪੂਰਨ ਰਿਵੀਜ਼ਨ ਨੋਟਸ",
                unitName = "Unit 1: Teaching Aptitude",
                pagesCount = 4,
                readTimeMinutes = 12,
                isFreePreview = true,
                pages = listOf(
                    DocumentPage(
                        pageNumber = 1,
                        heading = "1. Levels of Teaching (Memory, Understanding & Reflective)",
                        content = "Teaching takes place at three progressive levels:\n\n1. Memory Level (Thoughtless Teaching - Herbartian Model):\nFocuses on recall, recognition, and rote learning. Ideal for young learners and foundational factual knowledge.\n\n2. Understanding Level (Thoughtful Teaching - Morrison Model):\nFocuses on grasping relationships, applying facts, seeing principles, and generalizing.\n\n3. Reflective Level (Upper Thoughtful - Hunt Model):\nLearner confronts problems, formulates hypotheses, analyzes deeply, and acts autonomously.",
                        keyPoints = listOf(
                            "Memory: Herbart • Focus on rote retention and quick reproduction",
                            "Understanding: Morrison • Focus on concept mastery and generalization",
                            "Reflective: Hunt • Problem-solving and critical evaluation"
                        ),
                        punjabiSummary = "ਅਧਿਆਪਨ ਦੇ ਤਿੰਨ ਮੁੱਖ ਪੱਧਰ ਹਨ: ਯਾਦ ਸ਼ਕਤੀ ਪੱਧਰ (ਹਰਬਰਟ), ਸਮਝ ਪੱਧਰ (ਮੌਰੀਸਨ), ਅਤੇ ਚਿੰਤਨ ਪੱਧਰ (ਹੰਟ)। ਨੈੱਟ ਪ੍ਰੀਖਿਆ ਵਿੱਚ ਇਹਨਾਂ ਦੇ ਖੋਜੀ ਵਿਦਵਾਨਾਂ ਬਾਰੇ ਅਕਸਰ ਸਵਾਲ ਪੁੱਛੇ ਜਾਂਦੇ ਹਨ।",
                        hindiSummary = "शिक्षण के तीन प्रमुख स्तर: स्मृति स्तर (हर्बर्ट), अवबोध स्तर (मॉरिसन), और चिंतनशील स्तर (हंट)।"
                    ),
                    DocumentPage(
                        pageNumber = 2,
                        heading = "2. Methods of Teaching in Higher Education: Teacher vs Learner-Centric",
                        content = "Teacher-Centred Methods:\n• Lecture Method: Direct instruction, cost-effective for large lecture halls, but passive for students.\n• Demonstration: Visual model shown by instructor.\n• Team Teaching: Multiple subject experts instruct together.\n\nLearner-Centred & Digital Methods:\n• Swayam, Swayam Prabha, MOOCs (Massive Open Online Courses).\n• Flipped Classroom: Students learn concepts at home via videos and engage in discussions in class.\n• Heuristic / Inquiry-Based Method: Devised by H.E. Armstrong; learner acts as a discoverer.",
                        keyPoints = listOf(
                            "SWAYAM: Study Webs of Active-Learning for Young Aspiring Minds",
                            "SWAYAM PRABHA: 40 DTH Channels broadcasting 24x7 educational lectures",
                            "Flipped Classroom: Inverts traditional lecture & homework model"
                        ),
                        punjabiSummary = "ਵਿਦਿਆਰਥੀ-ਕੇਂਦਰਿਤ ਵਿਧੀਆਂ ਵਿੱਚ ਸਵੈਅਮ (SWAYAM), ਸਵੈਅਮ ਪ੍ਰਭਾ (SWAYAM PRABHA DTH ਚੈਨਲ), ਅਤੇ ਫਲਿੱਪਡ ਕਲਾਸਰੂਮ ਸਭ ਤੋਂ ਮਹੱਤਵਪੂਰਨ ਡਿਜੀਟਲ ਪਹਿਲਕਦਮੀਆਂ ਹਨ।",
                        hindiSummary = "शिक्षार्थी-केंद्रित विधियों में स्वयं (SWAYAM) और स्वयं प्रभा (SWAYAM PRABHA) उच्च शिक्षा के डिजिटल स्तंभ हैं।"
                    ),
                    DocumentPage(
                        pageNumber = 3,
                        heading = "3. Evaluation Systems & CBCS (Choice Based Credit System)",
                        content = "Types of Evaluation:\n• Placement: Before instruction to measure entry behavior.\n• Formative: During instruction (quizzes, ongoing feedback) to improve learning.\n• Diagnostic: Pinpoints persistent learning deficiencies and root difficulties.\n• Summative: At the conclusion of a term to certify, grade, or measure achievement.\n\nNorm-Referenced vs Criterion-Referenced:\n• Norm-Referenced: Compares a student's score against a peer group (e.g., Percentile, UGC NET Cutoff ranks).\n• Criterion-Referenced: Compares student achievement against an absolute standard or percentage (e.g., 60% first division).",
                        keyPoints = listOf(
                            "Formative = Assessment FOR Learning (Ongoing monitoring)",
                            "Summative = Assessment OF Learning (Final grade/certification)",
                            "Norm-Referenced = Percentile score relative to peers"
                        ),
                        punjabiSummary = "ਰਚਨਾਤਮਕ ਮੁਲਾਂਕਣ (Formative) ਸਿੱਖਣ ਦੌਰਾਨ ਸੁਧਾਰ ਲਈ ਹੁੰਦਾ ਹੈ, ਜਦਕਿ ਅੰਤਿਮ ਮੁਲਾਂਕਣ (Summative) ਸਮੈਸਟਰ ਦੇ ਅੰਤ ਵਿੱਚ ਗ੍ਰੇਡ ਦੇਣ ਲਈ ਕੀਤਾ ਜਾਂਦਾ ਹੈ।",
                        hindiSummary = "रचनात्मक मूल्यांकन (Formative) सीखने के दौरान फीडबैक प्रदान करता है, जबकि योगात्मक (Summative) अंतिम परिणाम तय करता है।"
                    ),
                    DocumentPage(
                        pageNumber = 4,
                        heading = "4. National Education Policy (NEP 2020) Higher Education Framework",
                        content = "Key NEP 2020 Reforms:\n• Gross Enrolment Ratio (GER) Target: Increase GER in higher education to 50% by 2035.\n• Single Umbrella Regulator: HECI (Higher Education Commission of India) with four verticals:\n  1. NHERC (Regulation)\n  2. NAC (Accreditation)\n  3. HEGC (Funding)\n  4. GEC (General Education Council - standard setting)\n• Academic Bank of Credits (ABC): Digital repository of student credits allowing multiple entry and exit points in undergraduate degrees.",
                        keyPoints = listOf(
                            "Target GER: 50% by year 2035",
                            "HECI: 4 verticals (NHERC, NAC, HEGC, GEC)",
                            "Academic Bank of Credits: APAAR / DigiLocker credit transfer"
                        ),
                        punjabiSummary = "ਨਵੀਂ ਸਿੱਖਿਆ ਨੀਤੀ (NEP 2020) ਅਧੀਨ 2035 ਤੱਕ ਉੱਚ ਸਿੱਖਿਆ ਵਿੱਚ ਜੀ.ਈ.ਆਰ. (GER) 50% ਕਰਨ ਦਾ ਟੀਚਾ ਰੱਖਿਆ ਗਿਆ ਹੈ ਅਤੇ ਸਿੰਗਲ ਰੈਗੂਲੇਟਰ HECI ਸਥਾਪਤ ਕੀਤਾ ਜਾ ਰਿਹਾ ਹੈ।",
                        hindiSummary = "एनईपी 2020 के तहत उच्च शिक्षा में 2035 तक जीईआर को 50% तक ले जाने का लक्ष्य है।"
                    )
                )
            ),
            StudyDocument(
                id = "doc_punjabi_gurmat",
                folderId = "f_ma_gurmat",
                courseCategory = CourseCategory.MA,
                title = "ਗੁਰਮਤਿ ਕਾਵਿ ਧਾਰਾ: ਦਾਰਸ਼ਨਿਕ ਪਰਿਪੇਖ ਤੇ ਪ੍ਰਮੁੱਖ ਬਾਣੀਕਾਰ",
                titlePa = "ਗੁਰਮਤਿ ਕਾਵਿ ਧਾਰਾ: ਸੰਪੂਰਨ ਅਧਿਐਨ",
                unitName = "ਭਾਗ 1: ਗੁਰਮਤਿ ਕਾਵਿ",
                pagesCount = 3,
                readTimeMinutes = 10,
                isFreePreview = true,
                pages = listOf(
                    DocumentPage(
                        pageNumber = 1,
                        heading = "1. ਗੁਰਮਤਿ ਕਾਵਿ ਦੀ ਉਤਪਤੀ ਤੇ ਪਿੱਠਭੂਮੀ",
                        content = "ਗੁਰਮਤਿ ਕਾਵਿ ਧਾਰਾ ਮੱਧਕਾਲੀਨ ਪੰਜਾਬੀ ਸਾਹਿਤ ਦੀ ਸਭ ਤੋਂ ਗੌਰਵਮਈ ਅਤੇ ਦਾਰਸ਼ਨਿਕ ਧਾਰਾ ਹੈ। ਇਸਦਾ ਆਰੰਭ ਸ਼੍ਰੀ ਗੁਰੂ ਨਾਨਕ ਦੇਵ ਜੀ (1469-1539) ਦੀ ਬਾਣੀ ਨਾਲ ਹੁੰਦਾ ਹੈ।\n\nਮੁੱਖ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ:\n• ਅਦਵੈਤਵਾਦੀ ਚਿੰਤਨ ਅਤੇ ਇੱਕ ਅਕਾਲ ਪੁਰਖ ਦੀ ਸਰਬ-ਵਿਆਪਕਤਾ।\n• ਕਰਮ-ਕਾਂਡਾਂ, ਵਰਣ-ਆਸ਼ਰਮ ਅਤੇ ਜਾਤ-ਪਾਤ ਦਾ ਖੰਡਨ।\n• ਸ਼ਬਦ-ਗੁਰੂ ਅਤੇ ਨਾਮ ਸਿਮਰਨ ਦਾ ਮਹੱਤਵ।\n• ਲੋਕ-ਕਾਵਿ ਰੂਪਾਂ (ਵਾਰ, ਬਾਰਹਮਾਹ, ਪੱਟੀ, ਅਲਾਹੁਣੀਆਂ, ਘੋੜੀਆਂ) ਦਾ ਰੂਪਾਂਤਰਣ।",
                        keyPoints = listOf(
                            "ਬਾਣੀਕਾਰ: ਗੁਰੂ ਨਾਨਕ ਦੇਵ ਜੀ, ਗੁਰੂ ਅੰਗਦ ਦੇਵ ਜੀ, ਗੁਰੂ ਅਮਰਦਾਸ ਜੀ, ਗੁਰੂ ਰਾਮਦਾਸ ਜੀ, ਗੁਰੂ ਅਰਜਨ ਦੇਵ ਜੀ, ਗੁਰੂ ਤੇਗ ਬਹਾਦਰ ਜੀ",
                            "ਗੁਰੂ ਗ੍ਰੰਥ ਸਾਹਿਬ ਦੀ ਸੰਪਾਦਨਾ: 1604 ਈ. (ਗੁਰੂ ਅਰਜਨ ਦੇਵ ਜੀ ਵੱਲੋਂ ਭਾਈ ਗੁਰਦਾਸ ਜੀ ਤੋਂ ਲਿਖਵਾਈ)",
                            "ਕੁੱਲ ਰਾਗ: 31 ਰਾਗ"
                        ),
                        punjabiSummary = "ਗੁਰਮਤਿ ਕਾਵਿ ਮਨੁੱਖੀ ਮੁਕਤੀ ਅਤੇ ਸੱਚੀ ਜੀਵਨ-ਜਾਚ ਦਾ ਮਾਰਗ ਦਰਸਾਉਂਦਾ ਹੈ। ਇਸਦਾ ਮੂਲ ਆਧਾਰ ਗੁਰੂ ਗ੍ਰੰਥ ਸਾਹਿਬ ਦੀ 31 ਰਾਗਾਂ ਵਿੱਚ ਦਰਜ ਬਾਣੀ ਹੈ।",
                        hindiSummary = "गुरमत काव्य धारा मध्यकालीन पंजाबी साहित्य की दार्शनिक रीढ़ है, जिसकी शुरुआत श्री गुरु नानक देव जी द्वारा हुई।"
                    ),
                    DocumentPage(
                        pageNumber = 2,
                        heading = "2. ਸ਼੍ਰੀ ਗੁਰੂ ਨਾਨਕ ਦੇਵ ਜੀ ਦੀਆਂ ਪ੍ਰਮੁੱਖ ਬਾਣੀਆਂ",
                        content = "1. ਜਪੁਜੀ ਸਾਹਿਬ: ਰਾਗ-ਰਹਿਤ ਸ਼ਾਹਕਾਰ ਰਚਨਾ। ਮੂਲ ਮੰਤਰ ਨਾਲ ਆਰੰਭ, 38 ਪਉੜੀਆਂ ਅਤੇ 2 ਸਲੋਕ। ਪੰਜ ਖੰਡਾਂ (ਧਰਮ ਖੰਡ, ਗਿਆਨ ਖੰਡ, ਸਰਮ ਖੰਡ, ਕਰਮ ਖੰਡ, ਸਚ ਖੰਡ) ਦਾ ਅਧਿਆਤਮਕ ਵਿਕਾਸ।\n\n2. ਆਸਾ ਦੀ ਵਾਰ: 24 ਪਉੜੀਆਂ, ਸਮਾਜਿਕ ਅਤੇ ਧਾਰਮਿਕ ਪਖੰਡਾਂ 'ਤੇ ਤਿੱਖਾ ਕਟਾਕਸ਼। ਔਰਤ ਦੀ ਮਹਾਨਤਾ ਬਾਰੇ ਸ਼ਬਦ: 'ਸੋ ਕਿਉ ਮੰਦਾ ਆਖੀਐ ਜਿਤੁ ਜੰਮਹਿ ਰਾਜਾਨ'।\n\n3. ਸਿੱਧ ਗੋਸਟਿ: ਯੋਗੀਆਂ ਨਾਲ ਰਾਮਕਲੀ ਰਾਗ ਵਿੱਚ ਸੰਵਾਦ। ਗ੍ਰਹਿਸਥ ਜੀਵਨ ਵਿੱਚ ਨਿਰਲੇਪ ਰਹਿਣ ਦਾ ਸੁਨੇਹਾ (ਜੈਸੇ ਜਲ ਮਹਿ ਕਮਲੁ ਨਿਰਾਲਮੁ)।\n\n4. ਬਾਰਹਮਾਹ ਤੁਖਾਰੀ: ਬਾਰਾਂ ਮਹੀਨਿਆਂ ਦੇ ਪ੍ਰਕਿਰਤਕ ਬਦਲਾਵਾਂ ਰਾਹੀਂ ਜੀਵ-ਇਸਤਰੀ ਦੀ ਪ੍ਰਭੂ-ਮਿਲਾਪ ਦੀ ਤਾਂਘ।",
                        keyPoints = listOf(
                            "ਜਪੁਜੀ ਸਾਹਿਬ: 38 ਪਉੜੀਆਂ + 2 ਸਲੋਕ (ਕੁੱਲ 40 ਅੰਗ)",
                            "ਪੰਜ ਖੰਡ: ਧਰਮ, ਗਿਆਨ, ਸਰਮ, ਕਰਮ, ਸਚ ਖੰਡ",
                            "ਆਸਾ ਦੀ ਵਾਰ: ਟੁੰਡੇ ਅਸਰਾਜੈ ਕੀ ਧੁਨੀ 'ਤੇ ਗਾਉਣ ਦਾ ਆਦੇਸ਼"
                        ),
                        punjabiSummary = "ਜਪੁਜੀ ਸਾਹਿਬ ਵਿੱਚ ਮਨੁੱਖ ਦੇ ਸੱਚਿਆਰ ਬਣਨ ਦਾ ਮਾਰਗ ਦਰਸਾਇਆ ਗਿਆ ਹੈ। ਆਸਾ ਦੀ ਵਾਰ ਸਮਾਜਿਕ ਚੇਤਨਾ ਅਤੇ ਬਰਾਬਰੀ ਦਾ ਪ੍ਰਤੀਕ ਹੈ।",
                        hindiSummary = "जपुजी साहिब में पांच आध्यात्मिक खंडों के माध्यम से सत्य का साक्षात्कार कराया गया है।"
                    )
                )
            ),
            StudyDocument(
                id = "doc_master_cadre_punjabi",
                folderId = "f_master_cadre_notes",
                courseCategory = CourseCategory.PUNJAB_GOVT,
                title = "ਪੰਜਾਬ ਮਾਸਟਰ ਕਾਡਰ: ਪੰਜਾਬੀ ਵਿਆਕਰਨ ਤੇ ਸ਼ਬਦ ਬੋਧ ਗੋਲਡਨ ਨੋਟਸ",
                titlePa = "ਮਾਸਟਰ ਕਾਡਰ ਪੰਜਾਬੀ ਵਿਆਕਰਨ ਗੋਲਡਨ ਨੋਟਸ",
                unitName = "ਯੂਨਿਟ 1: ਵਿਆਕਰਨ ਤੇ ਧੁਨੀ ਵਿਉਂਤ",
                pagesCount = 2,
                readTimeMinutes = 8,
                isFreePreview = true,
                pages = listOf(
                    DocumentPage(
                        pageNumber = 1,
                        heading = "1. ਧੁਨੀ ਵਿਉਂਤ (Phonetics & Phonology)",
                        content = "ਪੰਜਾਬੀ ਵਿੱਚ ਧੁਨੀਆਂ ਦੀ ਵੰਡ:\n\n1. ਖੰਡੀ ਧੁਨੀਆਂ (Segmental Phonemes):\n• ਸਵਰ (Vowels): 10 ਸਵਰ ਧੁਨੀਆਂ (ਅ, ਆ, ਇ, ਈ, ਉ, ਊ, ਏ, ਐ, ਓ, ਔ)। ਗੁਰਮੁਖੀ ਲਿਪੀ ਵਿੱਚ 3 ਸਵਰ ਵਾਹਕ (ੳ, ਅ, ੲ) ਹਨ।\n• ਵਿਅੰਜਨ (Consonants): ਕ ਤੋਂ ੜ ਤੱਕ।\n\n2. ਅਖੰਡੀ ਧੁਨੀਆਂ (Suprasegmental Phonemes):\n• ਸੁਰ (Tone): ਪੰਜਾਬੀ ਇੱਕ ਸੁਰਾਤਮਕ (Tonal) ਭਾਸ਼ਾ ਹੈ। ਤਿੰਨ ਸੁਰਾਂ: ਉੱਚੀ ਸੁਰ, ਮੱਧਮ ਸੁਰ, ਨੀਵੀਂ ਸੁਰ। ਘ, ਝ, ਢ, ਧ, ਭ ਧੁਨੀਆਂ ਸ਼ਬਦ ਦੇ ਸ਼ੁਰੂ ਵਿੱਚ ਹੋਣ 'ਤੇ ਸੁਰ ਪੈਦਾ ਕਰਦੀਆਂ ਹਨ।\n• ਨਾਸਿਕਤਾ (Nasalization): ਬਿੰਦੀ ਅਤੇ ਟਿੱਪੀ।\n• ਬਲ (Stress): ਅੱਧਕ।\n• ਵਾਕ-ਸੁਰ (Intonation) ਅਤੇ ਸੰਗਮ (Juncture)।",
                        keyPoints = listOf(
                            "ਸਵਰ ਵਾਹਕ = 3 (ੳ, ਅ, ੲ) | ਸਵਰ ਧੁਨੀਆਂ = 10",
                            "ਨਾਸਕੀ ਵਿਅੰਜਨ = 5 (ਙ, ਞ, ਣ, ਨ, ਮ)",
                            "ਪੰਜਾਬੀ ਇੱਕ ਸੁਰਾਤਮਕ (Tonal) ਭਾਸ਼ਾ ਹੈ।"
                        ),
                        punjabiSummary = "ਪੰਜਾਬੀ ਭਾਸ਼ਾ ਦੀ ਸਭ ਤੋਂ ਵਿਸ਼ੇਸ਼ ਗੱਲ ਇਸਦਾ ਸੁਰਾਤਮਕ ਹੋਣਾ ਹੈ। ਇਹ ਸਵਾਲ ਮਾਸਟਰ ਕਾਡਰ ਅਤੇ PSTET ਵਿੱਚ ਵਾਰ-ਵਾਰ ਪੁੱਛਿਆ ਜਾਂਦਾ ਹੈ।",
                        hindiSummary = "पंजाबी एक सुर-प्रधान (Tonal) भाषा है जिसमें 10 स्वर ध्वनियां और 3 स्वर वाहक हैं।"
                    )
                )
            )
        )
    )
    val documents: StateFlow<List<StudyDocument>> = _documents.asStateFlow()

    fun addStudyDocument(title: String, titlePa: String, folderId: String, category: CourseCategory, content: String) {
        val newDoc = StudyDocument(
            id = UUID.randomUUID().toString(),
            folderId = folderId,
            courseCategory = category,
            title = title,
            titlePa = if (titlePa.isBlank()) title else titlePa,
            unitName = "Uploaded Material",
            pagesCount = 1,
            readTimeMinutes = 5,
            isFreePreview = true,
            pages = listOf(
                DocumentPage(
                    pageNumber = 1,
                    heading = title,
                    content = content,
                    keyPoints = listOf("Recently added by Teacher Vision Academy", "Anti-piracy protected"),
                    punjabiSummary = "ਇਹ ਨੋਟਸ ਅਕੈਡਮੀ ਵੱਲੋਂ ਤਾਜ਼ਾ ਅਪਲੋਡ ਕੀਤੇ ਗਏ ਹਨ।"
                )
            )
        )
        _documents.value = listOf(newDoc) + _documents.value
    }

    // --- Quizzes & Mock Tests ---
    private val _quizzes = MutableStateFlow(
        listOf(
            Quiz(
                id = "quiz_ugc_p1_mock",
                title = "UGC NET Paper 1 All-India Mega Mock Test",
                titlePa = "ਯੂ.ਜੀ.ਸੀ. ਨੈੱਟ ਪੇਪਰ 1 ਆਲ-ਇੰਡੀਆ ਮੈਗਾ ਮੌਕ ਟੈਸਟ",
                category = CourseCategory.UGC_NET,
                durationMinutes = 15,
                totalQuestions = 5,
                marksPerQuestion = 2,
                negativeMarks = 0.5,
                questions = listOf(
                    QuizQuestion(
                        id = "q1",
                        questionEn = "Which of the following is the highest level of cognitive teaching according to Bloom's Revised Taxonomy?",
                        questionPa = "ਬਲੂਮ ਦੇ ਸੋਧੇ ਹੋਏ ਵਰਗੀਕਰਨ ਅਨੁਸਾਰ ਬੌਧਿਕ ਅਧਿਆਪਨ ਦਾ ਸਭ ਤੋਂ ਉੱਚਾ ਪੱਧਰ ਕਿਹੜਾ ਹੈ?",
                        questionHi = "ब्लूम के संशोधित वर्गीकरण के अनुसार संज्ञानात्मक शिक्षण का उच्चतम स्तर कौन सा है?",
                        optionsEn = listOf("Understanding", "Analyzing", "Creating", "Applying"),
                        optionsPa = listOf("ਸਮਝਣਾ (Understanding)", "ਵਿਸ਼ਲੇਸ਼ਣ ਕਰਨਾ (Analyzing)", "ਸਿਰਜਣਾ ਕਰਨਾ (Creating)", "ਲਾਗੂ ਕਰਨਾ (Applying)"),
                        optionsHi = listOf("समझना (Understanding)", "विश्लेषण (Analyzing)", "सृजन करना (Creating)", "लागू करना (Applying)"),
                        correctIndex = 2,
                        explanationEn = "In Anderson and Krathwohl's (2001) revised taxonomy, 'Creating' sits at the pinnacle, replacing 'Evaluation'.",
                        explanationPa = "ਬਲੂਮ ਦੀ ਸੋਧ ਕੀਤੀ ਟੈਕਸੋਨੋਮੀ (2001) ਵਿੱਚ 'ਸਿਰਜਣਾ' (Creating) ਸਭ ਤੋਂ ਉੱਚੇ ਪੱਧਰ 'ਤੇ ਆਉਂਦਾ ਹੈ।",
                        explanationHi = "संशोधित टैक्सोनॉमी (2001) में 'सृजन' (Creating) शीर्ष पर है।"
                    ),
                    QuizQuestion(
                        id = "q2",
                        questionEn = "SWAYAM PRABHA is an educational initiative broadcasting quality educational programs 24x7 through how many DTH channels?",
                        questionPa = "ਸਵੈਅਮ ਪ੍ਰਭਾ (SWAYAM PRABHA) ਕਿੰਨੇ ਡੀ.ਟੀ.ਐੱਚ. (DTH) ਚੈਨਲਾਂ ਰਾਹੀਂ 24x7 ਵਿਦਿਅਕ ਪ੍ਰੋਗਰਾਮ ਪ੍ਰਸਾਰਿਤ ਕਰਦਾ ਹੈ?",
                        questionHi = "स्वयं प्रभा (SWAYAM PRABHA) कितने डीटीएच चैनलों के माध्यम से 24x7 शैक्षिक कार्यक्रम प्रसारित करता है?",
                        optionsEn = listOf("34 Channels", "40 Channels", "32 Channels", "80 Channels"),
                        optionsPa = listOf("34 ਚੈਨਲ", "40 ਚੈਨਲ", "32 ਚੈਨਲ", "80 ਚੈਨਲ"),
                        optionsHi = listOf("34 चैनल", "40 चैनल", "32 चैनल", "80 चैनल"),
                        correctIndex = 1,
                        explanationEn = "The Ministry of Education upgraded SWAYAM PRABHA from 34 channels to 40 high-definition DTH channels telecasting curriculum-based content.",
                        explanationPa = "ਸਿੱਖਿਆ ਮੰਤਰਾਲੇ ਨੇ ਸਵੈਅਮ ਪ੍ਰਭਾ ਨੂੰ 34 ਤੋਂ ਵਧਾ ਕੇ 40 ਡੀ.ਟੀ.ਐੱਚ. ਚੈਨਲ ਕਰ ਦਿੱਤਾ ਹੈ।",
                        explanationHi = "शिक्षा मंत्रालय ने स्वयं प्रभा चैनलों की संख्या बढ़ाकर 40 डीटीएच चैनल कर दी है।"
                    ),
                    QuizQuestion(
                        id = "q3",
                        questionEn = "In which type of evaluation is a student's performance compared with an established absolute standard or percentage rather than other peers?",
                        questionPa = "ਕਿਹੜੇ ਮੁਲਾਂਕਣ ਵਿੱਚ ਵਿਦਿਆਰਥੀ ਦੀ ਕਾਰਗੁਜ਼ਾਰੀ ਦੂਜੇ ਵਿਦਿਆਰਥੀਆਂ ਦੀ ਬਜਾਏ ਇੱਕ ਨਿਰਧਾਰਤ ਮਾਪਦੰਡ (ਕ੍ਰਾਈਟੀਰੀਆ) ਨਾਲ ਤੁਲਨਾ ਕੀਤੀ ਜਾਂਦੀ ਹੈ?",
                        questionHi = "किस प्रकार के मूल्यांकन में छात्र के प्रदर्शन की तुलना अन्य छात्रों के बजाय एक निश्चित मानक से की जाती है?",
                        optionsEn = listOf("Norm-Referenced Testing", "Criterion-Referenced Testing", "Diagnostic Testing", "Continuous Evaluation"),
                        optionsPa = listOf("ਨਾਰਮ-ਰੈਫਰੈਂਸਡ ਟੈਸਟਿੰਗ", "ਕ੍ਰਾਈਟੀਰੀਅਨ-ਰੈਫਰੈਂਸਡ ਟੈਸਟਿੰਗ", "ਡਾਇਗਨੌਸਟਿਕ ਟੈਸਟਿੰਗ", "ਲਗਾਤਾਰ ਮੁਲਾਂਕਣ"),
                        optionsHi = listOf("मानक-संदर्भित (Norm-Referenced)", "मापदंड-संदर्भित (Criterion-Referenced)", "निदानात्मक परीक्षण", "सतत मूल्यांकन"),
                        correctIndex = 1,
                        explanationEn = "Criterion-Referenced Testing measures against a predefined criterion or objective mastery regardless of how others scored.",
                        explanationPa = "ਕ੍ਰਾਈਟੀਰੀਅਨ-ਰੈਫਰੈਂਸਡ ਟੈਸਟਿੰਗ ਵਿੱਚ ਵਿਦਿਆਰਥੀ ਦੇ ਅੰਕ ਪਹਿਲਾਂ ਤੋਂ ਤੈਅ ਮਿਆਰ (ਜਿਵੇਂ 60% ਜਾਂ ਪਾਸ ਅੰਕ) ਨਾਲ ਮਾਪੇ ਜਾਂਦੇ ਹਨ।",
                        explanationHi = "मापदंड-संदर्भित परीक्षण में प्रदर्शन को पूर्व-निर्धारित लक्ष्य या मानदंड से मापा जाता है।"
                    ),
                    QuizQuestion(
                        id = "q4",
                        questionEn = "Which committee or commission in India first recommended the 10+2+3 structure of education?",
                        questionPa = "ਭਾਰਤ ਵਿੱਚ ਕਿਸ ਕਮਿਸ਼ਨ ਨੇ ਸਭ ਤੋਂ ਪਹਿਲਾਂ ਸਿੱਖਿਆ ਦੇ 10+2+3 ਢਾਂਚੇ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਸੀ?",
                        questionHi = "भारत में किस आयोग ने पहली बार 10+2+3 शिक्षा संरचना की सिफारिश की थी?",
                        optionsEn = listOf("Radhakrishnan Commission (1948)", "Mudaliar Commission (1952)", "Kothari Commission (1964-66)", "National Knowledge Commission (2005)"),
                        optionsPa = listOf("ਰਾਧਾਕ੍ਰਿਸ਼ਨਨ ਕਮਿਸ਼ਨ (1948)", "ਮੁਦਾਲੀਅਰ ਕਮਿਸ਼ਨ (1952)", "ਕੋਠਾਰੀ ਕਮਿਸ਼ਨ (1964-66)", "ਨੈਸ਼ਨਲ ਨਾਲਿਜ ਕਮਿਸ਼ਨ (2005)"),
                        optionsHi = listOf("राधाकृष्णन आयोग (1948)", "मुदालियर आयोग (1952)", "कोठारी आयोग (1964-66)", "राष्ट्रीय ज्ञान आयोग (2005)"),
                        correctIndex = 2,
                        explanationEn = "The Kothari Commission (1964-66) headed by Dr. D.S. Kothari proposed the uniform 10+2+3 educational pattern across India.",
                        explanationPa = "ਕੋਠਾਰੀ ਕਮਿਸ਼ਨ (1964-66) ਨੇ ਭਾਰਤ ਵਿੱਚ 10+2+3 ਸਿੱਖਿਆ ਪ੍ਰਣਾਲੀ ਲਾਗੂ ਕਰਨ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਸੀ।",
                        explanationHi = "कोठारी आयोग (1964-66) ने देश भर में 10+2+3 संरचना की सिफारिश की थी।"
                    ),
                    QuizQuestion(
                        id = "q5",
                        questionEn = "What is the full form of APAAR ID launched under NEP 2020 for students?",
                        questionPa = "NEP 2020 ਤਹਿਤ ਵਿਦਿਆਰਥੀਆਂ ਲਈ ਸ਼ੁਰੂ ਕੀਤੀ ਗਈ APAAR ਆਈ.ਡੀ. ਦਾ ਪੂਰਾ ਨਾਮ ਕੀ ਹੈ?",
                        questionHi = "एनईपी 2020 के तहत शुरू की गई APAAR आईडी का पूर्ण रूप क्या है?",
                        optionsEn = listOf(
                            "Automated Permanent Academic Account Registry",
                            "All Primary Academic Assessment Record",
                            "Academic Pathway And Admission Record",
                            "Advanced Portal for All Academic Resources"
                        ),
                        optionsPa = listOf(
                            "Automated Permanent Academic Account Registry",
                            "All Primary Academic Assessment Record",
                            "Academic Pathway And Admission Record",
                            "Advanced Portal for All Academic Resources"
                        ),
                        optionsHi = listOf(
                            "Automated Permanent Academic Account Registry",
                            "All Primary Academic Assessment Record",
                            "Academic Pathway And Admission Record",
                            "Advanced Portal for All Academic Resources"
                        ),
                        correctIndex = 0,
                        explanationEn = "APAAR stands for 'Automated Permanent Academic Account Registry', also popularly known as 'One Nation, One Student ID'.",
                        explanationPa = "APAAR ਦਾ ਅਰਥ ਹੈ 'Automated Permanent Academic Account Registry', ਜਿਸਨੂੰ 'ਇੱਕ ਦੇਸ਼, ਇੱਕ ਵਿਦਿਆਰਥੀ ਆਈ.ਡੀ.' ਵੀ ਕਿਹਾ ਜਾਂਦਾ ਹੈ।",
                        explanationHi = "APAAR का अर्थ 'Automated Permanent Academic Account Registry' (एक राष्ट्र, एक छात्र आईडी) है।"
                    )
                )
            ),
            Quiz(
                id = "quiz_master_cadre_punjabi",
                title = "Punjab Master Cadre Punjabi Sahit Speed Test",
                titlePa = "ਪੰਜਾਬ ਮਾਸਟਰ ਕਾਡਰ ਪੰਜਾਬੀ ਸਾਹਿਤ ਸਪੀਡ ਟੈਸਟ",
                category = CourseCategory.PUNJAB_GOVT,
                durationMinutes = 10,
                totalQuestions = 3,
                marksPerQuestion = 2,
                negativeMarks = 0.5,
                questions = listOf(
                    QuizQuestion(
                        id = "pm_q1",
                        questionEn = "Who is considered the first Sufi poet of Punjabi literature?",
                        questionPa = "ਪੰਜਾਬੀ ਸਾਹਿਤ ਦਾ ਪਹਿਲਾ ਸੂਫ਼ੀ ਕਵੀ ਕਿਸਨੂੰ ਮੰਨਿਆ ਜਾਂਦਾ ਹੈ?",
                        questionHi = "पंजाबी साहित्य का पहला सूफ़ी कवि किसे माना जाता है?",
                        optionsEn = listOf("Shah Hussain", "Baba Farid Ji", "Bulleh Shah", "Sultan Bahu"),
                        optionsPa = listOf("ਸ਼ਾਹ ਹੁਸੈਨ", "ਬਾਬਾ ਸ਼ੇਖ਼ ਫ਼ਰੀਦ ਜੀ", "ਬੁੱਲ੍ਹੇ ਸ਼ਾਹ", "ਸੁਲਤਾਨ ਬਾਹੂ"),
                        optionsHi = listOf("शाह हुसैन", "बाबा शेख फरीद जी", "बुल्ले शाह", "सुल्तान बाहू"),
                        correctIndex = 1,
                        explanationEn = "Baba Sheikh Farid Ji (1173–1266) is universally acclaimed as the pioneer and first Sufi poet of Punjabi literature.",
                        explanationPa = "ਬਾਬਾ ਸ਼ੇਖ਼ ਫ਼ਰੀਦ ਜੀ (12ਵੀਂ-13ਵੀਂ ਸਦੀ) ਪੰਜਾਬੀ ਦੇ ਪਹਿਲੇ ਪ੍ਰਮਾਣਿਕ ਸੂਫ਼ੀ ਕਵੀ ਹਨ। ਉਹਨਾਂ ਦੇ 112 ਸਲੋਕ ਤੇ 4 ਸ਼ਬਦ ਗੁਰੂ ਗ੍ਰੰਥ ਸਾਹਿਬ ਵਿੱਚ ਦਰਜ ਹਨ।",
                        explanationHi = "बाबा शेख फरीद जी पंजाबी के प्रथम सूफ़ी कवि हैं।"
                    ),
                    QuizQuestion(
                        id = "pm_q2",
                        questionEn = "In Punjabi phonetics, which consonant sounds are known as flap / lateral sounds?",
                        questionPa = "ਪੰਜਾਬੀ ਧੁਨੀ ਵਿਉਂਤ ਵਿੱਚ 'ੜ' ਧੁਨੀ ਕਿਸ ਸ਼੍ਰੇਣੀ ਵਿੱਚ ਆਉਂਦੀ ਹੈ?",
                        questionHi = "पंजाबी ध्वन्यात्मकता में 'ੜ' ध्वनि किस श्रेणी में आती है?",
                        optionsEn = listOf("ਫਟਕਵਾਂ (Flap)", "ਕਾਂਬਵਾਂ (Trill)", "ਪਾਸੇਦਾਰ (Lateral)", "ਨਾਸਕੀ (Nasal)"),
                        optionsPa = listOf("ਫਟਕਵਾਂ ਵਿਅੰਜਨ (Flap)", "ਕਾਂਬਵਾਂ ਵਿਅੰਜਨ (Trill)", "ਪਾਸੇਦਾਰ ਵਿਅੰਜਨ (Lateral)", "ਨਾਸਕੀ ਵਿਅੰਜਨ (Nasal)"),
                        optionsHi = listOf("उत्क्षिप्त / फटकवां (Flap)", "प्रकंपी / कांबवां (Trill)", "पार्श्विक (Lateral)", "नासिक्य (Nasal)"),
                        correctIndex = 0,
                        explanationEn = "'ੜ' is a retroflex flap consonant in Punjabi phonetics.",
                        explanationPa = "'ੜ' ਪੰਜਾਬੀ ਵਿੱਚ ਫਟਕਵਾਂ ਵਿਅੰਜਨ ਹੈ, ਜਦਕਿ 'ਰ' ਕਾਂਬਵਾਂ ਅਤੇ 'ਲ' ਪਾਸੇਦਾਰ ਵਿਅੰਜਨ ਹੈ।",
                        explanationHi = "'ੜ' फटकवां (Flap) व्यंजन है और 'ਰ' प्रकंपी (Trill) है।"
                    ),
                    QuizQuestion(
                        id = "pm_q3",
                        questionEn = "Who wrote the famous Punjabi play 'Loha Kutt' (ਲੋਹਾ ਕੁੱਟ)?",
                        questionPa = "ਪੰਜਾਬੀ ਦਾ ਪ੍ਰਸਿੱਧ ਨਾਟਕ 'ਲੋਹਾ ਕੁੱਟ' ਕਿਸਦੀ ਰਚਨਾ ਹੈ?",
                        questionHi = "प्रसिद्ध पंजाबी नाटक 'लोहा कुट्ट' किसके द्वारा लिखा गया है?",
                        optionsEn = listOf("Ishwar Chander Nanda", "Balwant Gargi", "Harcharan Singh", "Sant Singh Sekhon"),
                        optionsPa = listOf("ਈਸ਼ਵਰ ਚੰਦਰ ਨੰਦਾ", "ਬਲਵੰਤ ਗਾਰਗੀ", "ਡਾ. ਹਰਚਰਨ ਸਿੰਘ", "ਸੰਤ ਸਿੰਘ ਸੇਖੋਂ"),
                        optionsHi = listOf("ईश्वर चंद्र नंदा", "बलवंत गार्गी", "डॉ. हरचरण सिंह", "संत सिंह सेखों"),
                        correctIndex = 1,
                        explanationEn = "'Loha Kutt' (1944) was the breakthrough debut play by Balwant Gargi, portraying rural Punjabi psychological realism.",
                        explanationPa = "'ਲੋਹਾ ਕੁੱਟ' (1944) ਬਲਵੰਤ ਗਾਰਗੀ ਦਾ ਪਹਿਲਾ ਨਾਟਕ ਸੀ ਜਿਸਨੇ ਪੰਜਾਬੀ ਰੰਗਮੰਚ ਵਿੱਚ ਯਥਾਰਥਵਾਦ ਦਾ ਨਵਾਂ ਦੌਰ ਸ਼ੁਰੂ ਕੀਤਾ।",
                        explanationHi = "'लोहा कुट्ट' बलवंत गार्गी का प्रसिद्ध यथार्थवादी नाटक है।"
                    )
                )
            )
        )
    )
    val quizzes: StateFlow<List<Quiz>> = _quizzes.asStateFlow()

    // --- Quiz Attempts History ---
    private val _attemptHistory = MutableStateFlow<List<QuizAttemptResult>>(emptyList())
    val attemptHistory: StateFlow<List<QuizAttemptResult>> = _attemptHistory.asStateFlow()

    fun recordAttempt(result: QuizAttemptResult) {
        _attemptHistory.value = listOf(result) + _attemptHistory.value
    }

    // --- Leaderboard ---
    val leaderboard = listOf(
        LeaderboardUser(1, "Amanpreet Kaur", "Bathinda, Punjab", 98, "96%", "🥇 Gold"),
        LeaderboardUser(2, "Harinder Singh", "Amritsar, Punjab", 96, "94%", "🥈 Silver"),
        LeaderboardUser(3, "Pooja Sharma", "Ludhiana, Punjab", 94, "92%", "🥉 Bronze"),
        LeaderboardUser(4, "Gurpreet Dhillon", "Patiala, Punjab", 92, "90%", "⭐ Top 5"),
        LeaderboardUser(5, "Rajesh Kumar", "Jalandhar, Punjab", 90, "88%", "⭐ Top 5"),
        LeaderboardUser(6, "Simranjeet Kaur", "Mohali, Punjab", 88, "86%", "Top 10"),
        LeaderboardUser(7, "Manjit Singh", "Sangrur, Punjab", 86, "85%", "Top 10")
    )

    // --- Live & Recorded Classes ---
    private val _liveClasses = MutableStateFlow(
        listOf(
            LiveClassItem(
                id = "live_1",
                title = "UGC NET Paper 1: Teaching Aptitude Rapid 50 Questions Live",
                subject = "Paper 1 General",
                educator = "Dr. Manpreet Singh (NET-JRF)",
                scheduledTime = "Daily Live @ 7:00 PM",
                isLiveNow = true,
                streamType = "YouTube Live",
                streamUrl = "https://youtube.com/live/teachervision",
                viewerCount = 284
            ),
            LiveClassItem(
                id = "live_2",
                title = "ਪੰਜਾਬੀ ਸਾਹਿਤ: ਗੁਰਮਤਿ ਕਾਵਿ ਧਾਰਾ ਤੇ ਵਿਸ਼ੇਸ਼ ਪ੍ਰਸ਼ਨੋਤਰੀ",
                subject = "Master Cadre & NET Punjabi",
                educator = "Prof. Jaswinder Kaur",
                scheduledTime = "Today @ 8:30 PM",
                isLiveNow = false,
                streamType = "Zoom Live Classroom",
                streamUrl = "https://zoom.us/j/teachervision7888",
                viewerCount = 190
            ),
            LiveClassItem(
                id = "rec_1",
                title = "Research Aptitude: Methods, Sampling & Hypothesis Explained",
                subject = "UGC NET Paper 1 Recorded",
                educator = "Dr. Manpreet Singh",
                scheduledTime = "Duration: 52 mins",
                isLiveNow = false,
                streamType = "Recorded Video Lecture",
                streamUrl = "https://teachervision.org/lectures/research-01",
                viewerCount = 1420
            ),
            LiveClassItem(
                id = "rec_2",
                title = "ਪੰਜਾਬੀ ਨਾਟਕ ਦਾ ਇਤਿਹਾਸ: ਈਸ਼ਵਰ ਚੰਦਰ ਨੰਦਾ ਤੋਂ ਬਲਵੰਤ ਗਾਰਗੀ ਤੱਕ",
                subject = "MA & Master Cadre Recorded",
                educator = "Prof. Jaswinder Kaur",
                scheduledTime = "Duration: 45 mins",
                isLiveNow = false,
                streamType = "Recorded Video Lecture",
                streamUrl = "https://teachervision.org/lectures/natak-history",
                viewerCount = 980
            )
        )
    )
    val liveClasses: StateFlow<List<LiveClassItem>> = _liveClasses.asStateFlow()

    // --- Student Roster for Admin ---
    private val _enrolledStudents = MutableStateFlow(
        listOf(
            StudentEnrollment("st_1", "Kiranjeet Kaur", "9876543210", CourseCategory.UGC_NET, "08 Oct 2026", true),
            StudentEnrollment("st_2", "Sukhdev Singh", "9814012345", CourseCategory.PUNJAB_GOVT, "07 Oct 2026", true),
            StudentEnrollment("st_3", "Navdeep Verma", "9417234567", CourseCategory.MA, "05 Oct 2026", true),
            StudentEnrollment("st_4", "Harmanpreet Singh", "7888213000", CourseCategory.UGC_NET, "01 Oct 2026", true),
            StudentEnrollment("st_5", "Deepika Rani", "9781234567", CourseCategory.B_ED, "28 Sep 2026", true),
            StudentEnrollment("st_6", "Jagjit Singh", "9872345678", CourseCategory.PUNJAB_GOVT, "26 Sep 2026", true)
        )
    )
    val enrolledStudents: StateFlow<List<StudentEnrollment>> = _enrolledStudents.asStateFlow()

    fun createQuiz(quiz: Quiz) {
        _quizzes.value = listOf(quiz) + _quizzes.value
    }
}
