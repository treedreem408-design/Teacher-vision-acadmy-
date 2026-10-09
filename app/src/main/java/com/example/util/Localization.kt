package com.example.util

import com.example.model.AppLanguage

object Localization {
    fun getText(key: String, lang: AppLanguage): String {
        val entry = dictionary[key] ?: return key
        return when (lang) {
            AppLanguage.ENGLISH -> entry.en
            AppLanguage.PUNJABI -> entry.pa
            AppLanguage.HINDI -> entry.hi
        }
    }

    private data class LangString(val en: String, val pa: String, val hi: String)

    private val dictionary = mapOf(
        "app_name" to LangString("Teacher Vision Academy", "ਟੀਚਰ ਵਿਜ਼ਨ ਅਕੈਡਮੀ", "टीचर विज़न अकादमी"),
        "tagline" to LangString("Your Success Our Priority", "ਤੁਹਾਡੀ ਸਫਲਤਾ ਸਾਡੀ ਪਹਿਲ", "आपकी सफलता हमारी प्राथमिकता"),
        "login_title" to LangString("Student Login", "ਵਿਦਿਆਰਥੀ ਲੌਗਇਨ", "विद्यार्थी लॉगिन"),
        "login_sub" to LangString("Enter your mobile number to start learning", "ਆਪਣਾ ਮੋਬਾਈਲ ਨੰਬਰ ਦਰਜ ਕਰੋ", "अपना मोबाइल नंबर दर्ज करें"),
        "mobile_hint" to LangString("Mobile Number (10 digits)", "ਮੋਬਾਈਲ ਨੰਬਰ (10 ਅੰਕ)", "मोबाइल नंबर (10 अंक)"),
        "get_otp" to LangString("Get OTP", "ਓ.ਟੀ.ਪੀ. ਪ੍ਰਾਪਤ ਕਰੋ", "ओटीपी प्राप्त करें"),
        "verify_otp" to LangString("Verify OTP", "ਓ.ਟੀ.ਪੀ. ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ", "ओटीपी सत्यापित करें"),
        "enter_otp" to LangString("Enter 4-digit OTP", "4-ਅੰਕਾਂ ਦਾ ਓ.ਟੀ.ਪੀ. ਦਰਜ ਕਰੋ", "4-अंकों का ओटीपी दर्ज करें"),
        "admin_quick_login" to LangString("Quick Admin Access (7888213000)", "ਡਾਇਰੈਕਟ ਐਡਮਿਨ ਲੌਗਇਨ (7888213000)", "क्विक एडमिन लॉगिन (7888213000)"),
        "student_name" to LangString("Your Full Name", "ਤੁਹਾਡਾ ਪੂਰਾ ਨਾਮ", "आपका पूरा नाम"),
        "select_course" to LangString("Select Target Course", "ਆਪਣਾ ਕੋਰਸ ਚੁਣੋ", "अपना कोर्स चुनें"),
        "continue_btn" to LangString("Continue to Classroom", "ਕਲਾਸਰੂਮ ਵਿੱਚ ਜਾਓ", "क्लासरूम में जाएं"),
        
        // Navigation & Home
        "nav_home" to LangString("Home", "ਹੋਮ", "होम"),
        "nav_courses" to LangString("Courses", "ਕੋਰਸ", "कोर्स"),
        "nav_material" to LangString("Study Material", "ਸਟੱਡੀ ਮਟੀਰੀਅਲ", "स्टडी मटेरियल"),
        "nav_tests" to LangString("Test Series", "ਟੈਸਟ ਸੀਰੀਜ਼", "टेस्ट सीरीज़"),
        "nav_profile" to LangString("Profile", "ਪ੍ਰੋਫਾਈਲ", "प्रोफाइल"),

        // Categories
        "cat_my_courses" to LangString("My Courses", "ਮੇਰੇ ਕੋਰਸ", "मेरे कोर्स"),
        "cat_live_classes" to LangString("Live Classes", "ਲਾਈਵ ਕਲਾਸਾਂ", "लाइव कक्षाएं"),
        "cat_tests" to LangString("Test Series & Quiz", "ਟੈਸਟ ਅਤੇ ਕੁਇਜ਼", "टेस्ट और क्विज़"),
        "cat_notes" to LangString("Folder Notes (PDF)", "ਨੋਟਸ (PDF)", "नोट्स (PDF)"),
        "cat_notifications" to LangString("Live Updates", "ਤਾਜ਼ਾ ਅਪਡੇਟਾਂ", "लाइव अपडेट"),
        "cat_admin" to LangString("Admin Panel", "ਐਡਮਿਨ ਪੈਨਲ", "एडमिन पैनल"),

        // Live Classes & Player
        "live_now" to LangString("LIVE NOW", "ਹੁਣ ਲਾਈਵ", "अभी लाइव"),
        "upcoming_classes" to LangString("Upcoming Classes", "ਆਉਣ ਵਾਲੀਆਂ ਕਲਾਸਾਂ", "आने वाली कक्षाएं"),
        "recorded_lectures" to LangString("Recorded Lectures", "ਰਿਕਾਰਡ ਕੀਤੇ ਲੈਕਚਰ", "रिकॉर्डेड लेक्चर्स"),
        "join_live" to LangString("Join Live Class", "ਲਾਈਵ ਕਲਾਸ ਵਿੱਚ ਜੁੜੋ", "लाइव क्लास में जुड़ें"),
        "watch_recording" to LangString("Watch Lecture", "ਲੈਕਚਰ ਦੇਖੋ", "लेक्चर देखें"),

        // Study Material
        "folder_title" to LangString("Course Folders", "ਕੋਰਸ ਫੋਲਡਰ", "कोर्स फोल्डर"),
        "unit_notes" to LangString("Unit-wise Notes", "ਯੂਨਿਟ ਅਨੁਸਾਰ ਨੋਟਸ", "यूनिट अनुसार नोट्स"),
        "read_pdf" to LangString("Read In-App PDF", "ਐਪ ਵਿੱਚ ਪੜ੍ਹੋ (PDF)", "ऐप में पढ़ें (PDF)"),
        "security_badge" to LangString("Anti-Piracy Protected", "ਸੁਰੱਖਿਅਤ ਦਸਤਾਵੇਜ਼", "सुरक्षित दस्तावेज़"),
        "no_download_notice" to LangString("In-app reader only • Download & screenshot restricted", "ਸਿਰਫ਼ ਐਪ ਵਿੱਚ ਉਪਲਬਧ • ਸਕ੍ਰੀਨਸ਼ੌਟ ਪਾਬੰਦੀਸ਼ੁਦਾ", "केवल ऐप में उपलब्ध • स्क्रीनशॉट प्रतिबंधित"),

        // Quiz
        "quiz_hub" to LangString("Quiz & Mock Tests", "ਕੁਇਜ਼ ਅਤੇ ਮੌਕ ਟੈਸਟ", "क्विज़ और मॉक टेस्ट"),
        "start_quiz" to LangString("Start Test", "ਟੈਸਟ ਸ਼ੁਰੂ ਕਰੋ", "टेस्ट शुरू करें"),
        "submit_quiz" to LangString("Submit Test", "ਟੈਸਟ ਜਮ੍ਹਾਂ ਕਰੋ", "टेस्ट सबमिट करें"),
        "question" to LangString("Question", "ਪ੍ਰਸ਼ਨ", "प्रश्न"),
        "time_remaining" to LangString("Time Left", "ਬਾਕੀ ਸਮਾਂ", "शेष समय"),
        "negative_marking_notice" to LangString("+2.0 Marks | -0.5 Negative", "+2.0 ਅੰਕ | -0.5 ਕਟੌਤੀ", "+2.0 अंक | -0.5 नेगेटिव"),
        "instant_result" to LangString("Result & Scorecard", "ਨਤੀਜਾ ਅਤੇ ਸਕੋਰ ਕਾਰਡ", "परिणाम और स्कोरकार्ड"),
        "leaderboard" to LangString("Leaderboard", "ਲੀਡਰਬੋਰਡ", "लीडरबोर्ड"),
        "view_solutions" to LangString("Review Solutions", "ਹੱਲ ਵੇਖੋ", "उत्तर और व्याख्या देखें"),

        // Security
        "flag_secure_active" to LangString("Screen Protection Enabled (FLAG_SECURE Active)", "ਸਕ੍ਰੀਨ ਸੁਰੱਖਿਆ ਚਾਲੂ ਹੈ", "स्क्रीन सुरक्षा सक्रिय है"),

        // Contact
        "helpline" to LangString("Helpline: 7888213000", "ਹੈਲਪਲਾਈਨ: 7888213000", "हेल्पलाइन: 7888213000"),
        "whatsapp_chat" to LangString("Chat on WhatsApp", "ਵਟਸਐਪ 'ਤੇ ਗੱਲ ਕਰੋ", "व्हाट्सएप पर चैट करें")
    )
}
