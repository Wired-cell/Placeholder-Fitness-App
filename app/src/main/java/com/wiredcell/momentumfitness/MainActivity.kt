package com.wiredcell.momentumfitness
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlin.random.Random
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { FitnessApp() } } }
private enum class Tab(val title:String,val icon:androidx.compose.ui.graphics.vector.ImageVector) {
 Home("Home",Icons.Default.Home), Train("Train",Icons.Default.FitnessCenter), Food("Food",Icons.Default.Restaurant), Track("Track",Icons.Default.WaterDrop), Shop("Shop",Icons.Default.ShoppingBag), Connect("Connect",Icons.Default.Groups)
}
private data class Lesson(val name:String,val desc:String)
private data class CommunityPost(val id:Int,val author:String,val message:String,val tag:String,val likes:Int,val pinned:Boolean=false)
private val originalPosts = listOf(
 CommunityPost(1,"Aarav M.","Day 12 of showing up. Could not finish a full push-up on Monday; today I managed three clean reps. Tiny win, massive mood boost.","Calisthenics • Original post",37,true),
 CommunityPost(2,"Mira S.","Tried a 10-minute stretch before work instead of scrolling. My shoulders feel less like concrete. Keeping this one.","Wellness • Original post",52,true),
 CommunityPost(3,"Kabir R.","Meal-prepped dal, rice and roasted vegetables for three lunches. Future me is extremely grateful.","Food • Original post",24,true),
 CommunityPost(4,"Nisha P.","First beginner boxing class done. Footwork is harder than it looks. Back next week!","Martial arts • Original post",68,true),
 CommunityPost(5,"Dev A.","Rest-day walk, 25 minutes, no pace target. Recovery is part of training too.","Recovery • Original post",41,true)
)
private fun makeRandomPost(id:Int):CommunityPost {
 val activities=listOf("a jab-cross combination","a roundhouse kick","a Muay Thai teep","a karate kata","a taekwondo front kick","a boxing footwork drill","a Brazilian jiu-jitsu shrimp escape","a vinyasa flow","a sun salutation","a downward dog","a warrior II pose","a tree pose","a cobra pose","a bridge pose","a plank hold","a handstand drill","a pull-up progression","a push-up set","a squat session","a skipping-rope workout","a mobility routine","a brisk morning walk","a breathing exercise","a yoga balance pose","a calisthenics circuit")
 val adjectives=listOf("good","bad","neutral","noti","traumatised","happy","sad","angry","smilling","gloomy","intelligent","persistent","handsome","beautiful","cute")
 val gender=listOf("boy","girl"); val names=listOf("Rohan K.","Anaya S.","Ishaan P.","Tara M.","Vihaan R.","Diya A.","Arjun D.","Meera N.","Reyansh B.","Aditi C.","Kunal J.","Sana V.","Advik L.","Ira G.")
 return CommunityPost(id,names.random(),"I did ${activities.random()} today. Piyush is a ${adjectives.random()} ${gender.random()}.","Community • just now",Random.nextInt(0,501))
}
private data class FoodLog(val name:String,val kcal:Int)
private data class Meal(val name:String,val tag:String,val kcal:Int,val protein:Int,val carbs:Int,val fat:Int,val ingredients:String,val method:String,val allergens:Set<String>)
@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun FitnessApp() {
 val context=LocalContext.current
 val prefs=remember { context.getSharedPreferences("momentum_profile",Context.MODE_PRIVATE) }
 var profileName by remember { mutableStateOf(prefs.getString("name","") ?: "") }
 var profileDob by remember { mutableStateOf(prefs.getString("dob","") ?: "") }
 var profileHeight by remember { mutableStateOf(prefs.getString("height","") ?: "") }
 var profileWeight by remember { mutableStateOf(prefs.getString("weight","") ?: "") }
 var profileLevel by remember { mutableStateOf(prefs.getString("level","Beginner") ?: "Beginner") }
 var profileHobbies by remember { mutableStateOf(prefs.getString("hobbies","") ?: "") }
 var profileEmail by remember { mutableStateOf(prefs.getString("google_email","") ?: "") }
 var profileComplete by remember { mutableStateOf(prefs.getBoolean("profile_complete",false)) }
 var onboardingStep by remember { mutableIntStateOf(0) }
 var tab by remember { mutableStateOf(Tab.Home) }; var dark by remember { mutableStateOf(true) }; var darcula by remember { mutableStateOf(true) }
 var streakDays by remember { mutableIntStateOf(0) }; var workoutsWeek by remember { mutableIntStateOf(0) }; var workoutMinutes by remember { mutableIntStateOf(0) }
 var foodLogs by remember { mutableStateOf(emptyList<FoodLog>()) }; var foodName by remember { mutableStateOf("") }; var foodKcal by remember { mutableStateOf("") }; var premiumOpen by remember { mutableStateOf(false) }
 var unlocked by remember { mutableStateOf(false) }; var settings by remember { mutableStateOf(false) }; var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
 var randomPosts by remember { mutableStateOf(emptyList<CommunityPost>()) }; var postCounter by remember { mutableIntStateOf(100) }
 LaunchedEffect(Unit) { while(true) { delay(150_000L); randomPosts=(listOf(makeRandomPost(postCounter))+randomPosts).take(20); postCounter++ } }
 var gateway by remember { mutableStateOf(false) }; var gatewayItems by remember { mutableStateOf(listOf<Pair<String,Int>>()) }
 var promo by remember { mutableStateOf("") }; var promoMsg by remember { mutableStateOf("") }; var water by remember { mutableIntStateOf(0) }; var mealCount by remember { mutableIntStateOf(0) }
 var allergies by remember { mutableStateOf(setOf<String>()) }; var cart by remember { mutableStateOf(setOf<String>()) }; var likes by remember { mutableStateOf(setOf<Int>()) }
 val bg=if(!dark) Color(0xFFF4F5F7) else if(darcula) Color(0xFF000000) else Color(0xFF101116)
 val surface=if(!dark) Color.White else if(darcula) Color(0xFF101014) else Color(0xFF1C1E27)
 val ink=if(dark) Color(0xFFF4F2FA) else Color(0xFF171923); val muted=if(dark) Color(0xFFB8B5C4) else Color(0xFF656978); val lime=Color(0xFFB9F36A)
 val lessons=listOf(
 Lesson("Boxing foundations","Footwork, guard and jab-cross • 18 min"),
 Lesson("Muay Thai basics","Stance, teep and roundhouse • 24 min"),
 Lesson("Brazilian Jiu-Jitsu","Escapes and positional basics • 21 min"),
 Lesson("Karate fundamentals","Kihon and controlled combinations • 16 min"),
 Lesson("Vinyasa flow","A steady full-body sequence • 22 min"),
 Lesson("Mobility & flexibility","Hips, shoulders and spine • 15 min"),
 Lesson("Restorative yoga","Gentle evening recovery • 20 min"),
 Lesson("Sun salutations","A guided Surya Namaskar • 12 min"),
 Lesson("Calisthenics starter","Push, pull and squat patterns • 19 min"),
 Lesson("Pull-up progression","Build toward your first pull-up • 17 min"),
 Lesson("Handstand pathway","Wrist prep and wall drills • 14 min"),
 Lesson("Core control","Hollow body and plank variations • 13 min"))
 val foods=listOf(
 Meal("Peanut-banana oats","Underweight • energy dense",520,20,72,18,"Oats 60g, milk 200ml, banana, peanut butter 1 tbsp, chia 1 tsp","Simmer oats in milk. Top with banana, peanut butter and chia.",setOf("Peanuts","Dairy")),
 Meal("Paneer rice bowl","Underweight • vegetarian",610,27,78,20,"Rice 1 cup, paneer 100g, peas ½ cup, curd ½ cup","Pan-sear paneer with spices. Serve over rice with peas and curd.",setOf("Dairy")),
 Meal("Moong dal khichdi","Average • balanced",430,18,66,10,"Rice ½ cup, moong dal ½ cup, carrot, peas, cumin, oil 1 tsp","Rinse rice and dal. Pressure-cook with vegetables, cumin and water.",emptySet()),
 Meal("Chickpea quinoa salad","Average • active day",490,21,69,15,"Quinoa ¾ cup, chickpeas ¾ cup, cucumber, tomato, lemon, olive oil","Combine cooked quinoa and chickpeas with vegetables; dress with lemon.",emptySet()),
 Meal("Grilled tofu millet plate","Overweight • fibre forward",440,25,52,14,"Tofu 150g, cooked millet ¾ cup, mixed vegetables 2 cups","Grill seasoned tofu. Serve with millet and steamed vegetables.",setOf("Soy")),
 Meal("Dal & vegetable roti","Obesity class I • portion aware",460,22,61,13,"Masoor dal 1 cup, 2 small whole-wheat rotis, vegetables, salad","Cook dal with tomato and spices. Serve with rotis and salad.",setOf("Gluten")),
 Meal("Egg bhurji & roti","Obesity class II • protein rich",410,26,38,15,"2 eggs, onion, tomato, spinach, 1 small roti","Scramble eggs with vegetables. Serve with roti and cucumber.",setOf("Eggs","Gluten")),
 Meal("Lentil & sweet potato","All body types • plant based",475,19,76,11,"Lentils 1 cup, sweet potato 150g, spinach, tahini 1 tsp","Roast sweet potato. Add warm lentils and spinach; finish with tahini.",setOf("Sesame")))

 val expandedFoods=foods+listOf(
 Meal("Masala egg oats","Protein breakfast",390,24,42,13,"Rolled oats, 2 eggs, onion, tomato, turmeric","Cook oats with water and spices. Scramble eggs with vegetables and fold through.",setOf("Eggs")),
 Meal("Ragi dosa with sambar","South Indian",420,14,68,10,"Ragi flour, urad batter, sambar","Cook dosas on a hot pan and serve with vegetable sambar.",setOf("Legumes")),
 Meal("Idli and vegetable sambar","Balanced breakfast",360,13,66,5,"3 idlis, sambar, coconut chutney","Steam idlis and serve with hot vegetable sambar.",setOf("Coconut")),
 Meal("Poha with peas","Quick breakfast",340,9,58,9,"Flattened rice, peas, peanuts, onion, lemon","Rinse poha, sauté aromatics and peas, fold in poha and finish with lemon.",setOf("Peanuts")),
 Meal("Besan chilla","Vegetarian protein",310,17,38,9,"Besan, onion, tomato, spinach, curd","Whisk besan with water and vegetables. Cook savoury pancakes.",setOf("Legumes","Dairy")),
 Meal("Sprouted moong chaat","Fresh snack",230,14,35,4,"Moong sprouts, tomato, cucumber, lemon","Steam sprouts lightly if preferred; toss with vegetables, lemon and spices.",setOf("Legumes")),
 Meal("Rajma brown rice bowl","Plant protein",510,20,82,10,"Rajma, brown rice, tomato, onion, salad","Simmer rajma in tomato masala and serve with brown rice and salad.",setOf("Legumes")),
 Meal("Palak tofu","Plant based",390,24,32,18,"Tofu, spinach, tomato, garlic, millet roti","Blend cooked spinach, simmer with garlic and tomato, then add tofu.",setOf("Soy")),
 Meal("Chana masala","Chickpea curry",460,19,72,11,"Chickpeas, tomato, onion, spices, roti","Simmer chickpeas in onion-tomato masala until thick. Serve with roti.",setOf("Legumes","Gluten")),
 Meal("Vegetable dalia","One-pot meal",370,13,66,7,"Broken wheat, carrots, beans, peas, cumin","Sauté vegetables and dalia, add water and pressure-cook until soft.",setOf("Gluten")),
 Meal("Lemon rice with peanuts","Quick lunch",430,10,68,13,"Rice, peanuts, curry leaves, lemon, turmeric","Temper mustard, curry leaves and peanuts; toss with rice and lemon.",setOf("Peanuts")),
 Meal("Curd rice with pomegranate","Cooling meal",380,12,62,9,"Rice, plain curd, pomegranate, curry leaves","Fold curd into cooled rice and top with pomegranate.",setOf("Dairy")),
 Meal("Grilled chicken millet bowl","High protein",520,39,55,15,"Chicken breast, millet, vegetables, yoghurt marinade","Grill marinated chicken thoroughly and serve with millet and vegetables.",setOf("Dairy")),
 Meal("Fish curry with rice","Protein rich",490,32,61,12,"Fish, tomato, tamarind, spices, rice","Simmer fish gently in tomato-tamarind curry until cooked through.",setOf("Fish")),
 Meal("Paneer bhurji wrap","Portable lunch",480,25,49,19,"Paneer, whole-wheat wrap, capsicum, onion, tomato","Scramble paneer with vegetables and spices; roll into a warm wrap.",setOf("Dairy","Gluten")),
 Meal("Sweet potato chaat","Pre-workout snack",260,5,52,5,"Sweet potato, lemon, cumin, coriander, yoghurt","Steam or roast sweet potato, cube and toss with spices and lemon.",setOf("Dairy")),
 Meal("Banana lassi","Quick energy",250,9,42,6,"Banana, plain yoghurt, water, cardamom","Blend all ingredients until smooth; serve chilled.",setOf("Dairy")),
 Meal("Apple cinnamon yoghurt","Simple snack",190,10,29,4,"Apple, plain yoghurt, cinnamon, pumpkin seeds","Dice apple and serve over yoghurt with cinnamon and seeds.",setOf("Dairy","Seeds")),
 Meal("Roasted makhana","Crunchy snack",170,5,22,7,"Makhana, ghee, turmeric, pepper","Dry-roast makhana, add ghee and spices, toss until crisp.",setOf("Dairy")),
 Meal("Hummus vegetable sticks","Fibre snack",220,8,25,10,"Chickpeas, tahini, lemon, carrot and cucumber","Blend chickpeas with tahini and lemon. Serve with vegetable sticks.",setOf("Sesame","Legumes")),
 Meal("Overnight chia oats","Make ahead",410,15,54,16,"Oats, chia, milk, berries, nuts","Combine oats, chia and milk; chill overnight and top with fruit.",setOf("Dairy","Tree nuts")),
 Meal("Berry peanut smoothie","Recovery snack",330,14,43,12,"Berries, milk, peanut butter, oats","Blend until smooth. Adjust thickness with water or milk.",setOf("Peanuts","Dairy")),
 Meal("Vegetable upma","Warm breakfast",350,10,56,10,"Semolina, mixed vegetables, mustard, curry leaves","Toast semolina. Temper spices and vegetables, add water and stir.",setOf("Gluten")),
 Meal("Tofu scramble toast","Plant breakfast",360,22,38,13,"Tofu, wholegrain toast, spinach, turmeric","Crumble tofu and sauté with spinach and spices. Serve on toast.",setOf("Soy","Gluten")),
 Meal("Oats khichdi","Light meal",340,14,55,8,"Rolled oats, moong dal, carrots, peas, cumin","Cook dal and vegetables, add oats and simmer until creamy.",setOf("Legumes")),
 Meal("Stuffed vegetable paratha","Filling breakfast",430,12,62,14,"Whole-wheat dough, carrot, cauliflower, curd","Fill dough with spiced vegetables, roll and cook on a hot pan.",setOf("Gluten","Dairy")),
 Meal("Black bean corn salad","Fresh plant meal",390,17,61,9,"Black beans, corn, tomato, capsicum, lime","Rinse beans and toss with corn, vegetables and lime dressing.",setOf("Legumes")),
 Meal("Pumpkin lentil soup","Comforting dinner",320,17,48,7,"Pumpkin, red lentils, ginger, stock, pepper","Simmer pumpkin and lentils until tender; blend partly or fully.",setOf("Legumes")),
 Meal("Vegetable pulao with raita","Balanced meal",470,13,75,12,"Basmati rice, mixed vegetables, spices, curd","Cook rice with vegetables and whole spices; serve with raita.",setOf("Dairy")),
 Meal("Grilled corn paneer salad","Protein salad",400,23,38,18,"Paneer, corn, lettuce, tomato, lime","Grill paneer and corn, then toss with salad vegetables and lime.",setOf("Dairy")),
 Meal("Methi dal with roti","Everyday dinner",440,19,65,11,"Toor dal, fenugreek leaves, whole-wheat roti","Cook dal and methi with spices. Serve with a warm roti.",setOf("Legumes","Gluten")),
 Meal("Coconut vegetable stew","Gentle dinner",390,8,43,22,"Mixed vegetables, light coconut milk, curry leaves","Simmer vegetables in coconut milk with ginger and curry leaves.",setOf("Coconut"))
 )

 val products=listOf(
 Triple("Kimjaly 6mm Yoga Mat","₹999",999),
 Triple("Boldfit NBR Yoga Mat 8mm","₹899",899),
 Triple("Yoga Alignment Mat","₹1,575",1575),
 Triple("DOMYOS Resistance Band Set","₹1,999",1999),
 Triple("Mini Loop Resistance Bands","₹399",399),
 Triple("Latex Resistance Tube 15kg","₹299",299),
 Triple("Hex Dumbbells 2kg Pair","₹400",400),
 Triple("Hex Dumbbells 3kg Pair","₹544",544),
 Triple("Adjustable Dumbbell Set","₹2,499",2499),
 Triple("DOMYOS Skipping Rope","₹299",299),
 Triple("Speed Jump Rope","₹499",499),
 Triple("Textured Foam Roller","₹799",799),
 Triple("Yoga Block","₹244",244),
 Triple("Yoga Strap","₹189",189),
 Triple("Adjustable Hand Gripper","₹199",199),
 Triple("Push-Up Bar Grips","₹499",499),
 Triple("Doorway Pull-Up Bar","₹1,249",1249),
 Triple("Gym Ball with Pump","₹579",579),
 Triple("Yoga Wheel","₹730",730),
 Triple("Massage Ball Set","₹399",399),
 Triple("Advanced Yoga Video Course","₹499",499),
 Triple("Strength Training Video Course","₹599",599),
 Triple("Martial Arts Video Course","₹699",699),
 Triple("30-Day Asana Mastery","₹899",899),
 Triple("12-Week Strength Foundations","₹1,299",1299),
 Triple("Mobility & Recovery Deep Dive","₹699",699),
 Triple("Handstand Pathway","₹799",799),
 Triple("Nutrition & Meal Planning","₹599",599)
 )
 MaterialTheme(colorScheme=if(dark) darkColorScheme(primary=lime,background=bg,surface=surface,onBackground=ink,onSurface=ink) else lightColorScheme(primary=Color(0xFF426B18),background=bg,surface=surface,onBackground=ink,onSurface=ink)) {
 Scaffold(containerColor=bg,topBar={ TopAppBar(title={Text(if(settings) "Settings" else if(premiumOpen) "Premium courses" else if(selectedLesson!=null) selectedLesson!!.name else "MOMENTUM",fontWeight=FontWeight.Black,letterSpacing=1.2.sp)},actions={if(profileComplete)IconButton(onClick={settings=!settings}){Icon(if(settings) Icons.Default.Close else Icons.Default.Settings,"Settings")} },colors=TopAppBarDefaults.topAppBarColors(containerColor=bg,titleContentColor=ink,actionIconContentColor=ink)) },
 bottomBar={if(profileComplete && !settings && selectedLesson==null && !gateway && !premiumOpen) NavigationBar(containerColor=surface){Tab.entries.forEach{t->NavigationBarItem(selected=tab==t,onClick={tab=t},icon={Icon(t.icon,t.title)},label={Text(t.title,fontSize=10.sp)})}}}) { pad ->
 Box(Modifier.fillMaxSize().padding(pad)) {
 when {
 premiumOpen -> PremiumPaywall(ink,muted,surface,cart,{n->cart=if(n in cart)cart-n else cart+n},{gatewayItems=cart.mapNotNull{n->products.find{p->p.first==n}?.let{it.first to it.third}};gateway=true},{premiumOpen=false})
 selectedLesson != null -> CourseDetail(selectedLesson!!,ink,muted,surface,{selectedLesson=null})
 settings -> SettingsScreen(dark,darcula,profileName,profileDob,profileHeight,profileWeight,profileLevel,profileHobbies,profileEmail,{dark=it;if(!it)darcula=false},{darcula=it;dark=true},ink,muted,surface)
 gateway -> Checkout(gatewayItems,ink,muted,surface,{gateway=false})
 !profileComplete -> OnboardingScreen(onboardingStep,{onboardingStep=it},profileName,{profileName=it},profileDob,{profileDob=it},profileHeight,{profileHeight=it},profileWeight,{profileWeight=it},profileLevel,{profileLevel=it},profileHobbies,{profileHobbies=it},profileEmail,{profileEmail=it},prefs,{profileComplete=true},ink,muted,surface)
 else -> when(tab) {
 Tab.Home -> HomeScreen(ink,muted,surface,lime,unlocked,streakDays,workoutsWeek,expandedFoods.size,profileHeight,profileWeight,{tab=it})
 Tab.Train -> TrainingScreen(lessons,ink,muted,surface,lime,cart,{n->cart=if(n in cart)cart-n else cart+n},{l->selectedLesson=l})
 Tab.Food -> FoodScreen(expandedFoods,allergies,{allergies=it},ink,muted,surface)
 Tab.Track -> Tracker(water,{water=(water+1).coerceAtMost(20)},{water=(water-1).coerceAtLeast(0)},mealCount,{mealCount++},foodLogs,{name,kcal->foodLogs=foodLogs+FoodLog(name,kcal);mealCount++},foodName,{foodName=it},foodKcal,{foodKcal=it},streakDays,workoutsWeek,workoutMinutes,{streakDays++;workoutsWeek++;workoutMinutes+=30},ink,muted,surface)
 Tab.Shop -> Shop(products,cart,{cart=if(it in cart)cart-it else cart+it},ink,muted,surface,{gatewayItems=cart.mapNotNull{n->products.find{p->p.first==n}?.let{it.first to it.third}};gateway=true})
 Tab.Connect -> Community(randomPosts,likes,{i->likes=if(i in likes)likes-i else likes+i},ink,muted,surface)
 }
 }
 if(profileComplete && !settings && !gateway && selectedLesson==null && !premiumOpen) PremiumShelf(Modifier.align(Alignment.BottomCenter),surface,ink,muted,{premiumOpen=true})
 }
 }
 }
}

private val asanaNames=listOf("Mountain Pose (Tadasana)","Raised Arms (Urdhva Hastasana)","Standing Forward Fold (Uttanasana)","Half Lift (Ardha Uttanasana)","Downward Dog (Adho Mukha Svanasana)","Plank (Phalakasana)","Four-Limbed Staff (Chaturanga)","Cobra (Bhujangasana)","Upward Dog (Urdhva Mukha Svanasana)","Child's Pose (Balasana)","Warrior I (Virabhadrasana I)","Warrior II (Virabhadrasana II)","Warrior III (Virabhadrasana III)","Extended Side Angle","Triangle (Trikonasana)","Reverse Warrior","Extended Hand-to-Big-Toe","Tree (Vrksasana)","Eagle (Garudasana)","Chair (Utkatasana)","Goddess (Utkata Konasana)","Garland (Malasana)","Low Lunge (Anjaneyasana)","Half Split (Ardha Hanumanasana)","Pigeon (Eka Pada Rajakapotasana)","Bridge (Setu Bandha)","Locust (Salabhasana)","Bow (Dhanurasana)","Camel (Ustrasana)","Seated Forward Fold (Paschimottanasana)","Bound Angle (Baddha Konasana)","Staff (Dandasana)","Seated Twist (Ardha Matsyendrasana)","Boat (Navasana)","Happy Baby (Ananda Balasana)","Supine Twist","Legs-Up-the-Wall (Viparita Karani)","Corpse (Savasana)","Dolphin (Ardha Pincha Mayurasana)","Supported Shoulder Bridge")
private val asanaInstructions=listOf(
"Stand tall with feet hip-width, distribute weight evenly, engage thighs gently and lengthen through crown of head. Hold 5 breaths.",
"Stand grounded, inhale and sweep arms overhead without flaring ribs; soften shoulders and gaze forward. Hold 3–5 breaths.",
"Fold from hips with knees soft; let head relax and place hands on blocks or shins. Rise slowly.",
"Hands on shins, lengthen spine forward and keep neck long; avoid rounding lower back.",
"Hands shoulder-width, hips lift back and up; bend knees to lengthen spine, press floor away. Hold 5 breaths.",
"Hands under shoulders, legs long; brace abdomen, keep hips level and press floor away. Modify with knees down.",
"From plank, bend elbows close to ribs and lower only as far as shoulders stay controlled; knees down is a valid option.",
"Lie prone, hands beside lower ribs; gently lift chest using back muscles, elbows bent, pelvis grounded. Avoid compressing low back.",
"From prone, press hands and tops of feet, lift thighs and chest only if spine and wrists tolerate; otherwise use cobra.",
"Kneel, bring big toes together and knees comfortable apart; sit hips toward heels and rest forehead on support.",
"Step one foot forward, back heel grounded; bend front knee, lift arms only if shoulders comfortable and keep hips facing forward.",
"Wide stance, front toes out; bend front knee tracking over toes, extend arms and gaze over front hand.",
"From Warrior I, hinge forward and lift back leg; keep hips level and hands on blocks or wall for balance.",
"From Warrior II, hinge over front leg, forearm to thigh or hand to block; extend top arm without collapsing chest.",
"Stand wide, turn front foot out; reach forward then hinge, hand to shin/block and chest open sideways.",
"From Warrior II, keep front knee bent and arc front arm overhead; side body lengthens without crunching lower back.",
"Hold a wall for support, lift knee and gently extend foot forward; use strap only if comfortable, no forcing.",
"Stand tall, place foot on calf or inner thigh (never knee), use wall support and keep pelvis level.",
"Stand on one leg, cross other thigh and optionally wrap foot; cross arms and balance near a wall.",
"Feet hip-width, sit hips back as if chair; keep knees tracking over toes and ribs stacked above pelvis.",
"Wide stance, bend knees tracking toes and sit hips down; keep torso upright and hands at heart.",
"Stand wide, turn toes out and lower hips only as far as knees remain comfortable; use a block under seat.",
"Kneel with one foot forward, tuck pelvis gently and shift forward until front hip stretches; avoid arching back.",
"From low lunge, shift hips back and straighten front leg with a soft knee; hinge forward from hips.",
"From tabletop, bring one knee forward toward wrist and extend other leg back; support front hip with cushion and avoid knee pain.",
"Lie on back, knees bent and feet parallel; press feet down and lift hips without turning knees outward.",
"Lie prone, arms by sides; lift chest and legs a little using back muscles, keep neck neutral.",
"Lie prone, bend knees and reach back only if shoulders allow; lift chest gently, avoid pulling feet forcefully.",
"Kneel with knees hip-width, hands at pelvis or supported blocks; lift chest and lean back only within comfortable range.",
"Sit tall, soles together and knees supported; hinge forward gently from hips without pressing knees down.",
"Sit with legs extended, hands beside hips; flex feet gently and lengthen spine.",
"Sit tall, bend one knee and place foot outside opposite thigh; rotate gently from ribs, keep spine long.",
"Sit with knees bent, lean back until core engages; lift shins parallel or keep toes down, spine long.",
"Lie on back, draw knees toward chest and hold behind thighs; relax shoulders and breathe.",
"Lie on back, knees bent; let knees fall to one side while shoulders remain grounded, switch sides.",
"Sit beside wall and swing legs up; move hips to comfortable distance and exit slowly.",
"Lie flat, legs relaxed and arms by sides; release effort and breathe naturally for several minutes.",
"From tabletop, place forearms down and lift hips; keep neck relaxed and knees bent if needed.",
"Lie on back, feet grounded and knees bent; lift hips gently and hold, or keep hips down if back feels strained."
)
@Composable private fun AsanaLibrary(ink:Color,muted:Color,surface:Color){
 var q by remember{mutableStateOf("")};var expanded by remember{mutableStateOf("")}
 val shown=asanaNames.filter{it.contains(q,true)}
 Column(verticalArrangement=Arrangement.spacedBy(7.dp)){Text("40-POSE ASANA LIBRARY",color=ink,fontWeight=FontWeight.Bold,fontSize=16.sp);OutlinedTextField(value=q,onValueChange={q=it},label={Text("Search poses")},modifier=Modifier.fillMaxWidth(),singleLine=true)
 shown.forEach{pose->val i=asanaNames.indexOf(pose);Card(Modifier.fillMaxWidth().clickable{expanded=if(expanded==pose)"" else pose},colors=CardDefaults.cardColors(containerColor=surface),shape=RoundedCornerShape(12.dp)){Column(Modifier.padding(11.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(pose,color=ink,fontWeight=FontWeight.SemiBold,fontSize=13.sp,modifier=Modifier.weight(1f));Icon(if(expanded==pose)Icons.Default.ExpandLess else Icons.Default.ExpandMore,null,tint=muted)};if(expanded==pose){Text("How to practise",color=ink,fontWeight=FontWeight.Bold,fontSize=12.sp);Text(asanaInstructions[i],color=muted,fontSize=12.sp);Text("Breathe normally. Use props or a smaller range, and stop if you feel pain, numbness or dizziness.",color=muted,fontSize=11.sp)}}}}
 }
}
@Composable private fun YouTubeLibrary(ink:Color,muted:Color,surface:Color,cart:Set<String>,toggleCart:(String)->Unit){
 val context=LocalContext.current
 val freeVideos=listOf("20 Min Yoga Flow • Charlie Follows" to "https://www.youtube.com/watch?v=fxT-5bmzWbk","20 Min Low Impact Cardio • Heather Robertson" to "https://www.youtube.com/watch?v=cA11MYXhBfI","Beginner Bodyweight Workout • Sunny Health" to "https://www.youtube.com/watch?v=WrXeb6EZrK0","10 Min Sun Salutation • Vedam" to "https://www.youtube.com/watch?v=hJolNd62Qhs","Muay Thai 101 • Sean Fagan" to "https://www.youtube.com/watch?v=640rsP_0WgY")
 val paidVideos=listOf("Advanced Yoga Video Course" to "₹499","Strength Training Video Course" to "₹599","Martial Arts Video Course" to "₹699")
 Column(verticalArrangement=Arrangement.spacedBy(7.dp)){
 Text("FREE VIDEO GUIDES",color=ink,fontWeight=FontWeight.Bold,fontSize=16.sp)
 freeVideos.forEach{(title,url)->Card(colors=CardDefaults.cardColors(containerColor=surface),shape=RoundedCornerShape(12.dp)){Row(Modifier.fillMaxWidth().clickable{context.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(url)))}.padding(11.dp),verticalAlignment=Alignment.CenterVertically){Text("▶",color=Color(0xFFE45C54),fontSize=21.sp);Spacer(Modifier.width(9.dp));Column(Modifier.weight(1f)){Text(title,color=ink,fontWeight=FontWeight.SemiBold,fontSize=13.sp);Text("FREE • YouTube",color=Color(0xFF8BAF52),fontSize=10.sp,fontWeight=FontWeight.Bold)};Icon(Icons.Default.OpenInNew,null,tint=muted)}}}
 Text("PAID VIDEO COURSES",color=ink,fontWeight=FontWeight.Bold,fontSize=16.sp)
 paidVideos.forEach{(title,price)->Panel(surface){Row(Modifier.padding(12.dp),verticalAlignment=Alignment.CenterVertically){Text("🎬",fontSize=25.sp);Column(Modifier.weight(1f).padding(horizontal=9.dp)){Text(title,color=ink,fontWeight=FontWeight.Bold,fontSize=13.sp);Text(price+" • Full course access",color=muted,fontSize=11.sp)};Button(onClick={toggleCart(title)}){Text(if(title in cart)"Remove" else "Add to cart")}}}}
 }
}
@Composable private fun PremiumShelf(modifier:Modifier,surface:Color,ink:Color,muted:Color,onClick:()->Unit){Column(modifier.fillMaxWidth().background(Color(0xE9101116),RoundedCornerShape(topStart=16.dp,topEnd=16.dp)).clickable(onClick=onClick).padding(10.dp),verticalArrangement=Arrangement.spacedBy(4.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text("🔒 PREMIUM COURSE COLLECTION",color=Color(0xFFE8B75A),fontWeight=FontWeight.Black,fontSize=10.sp,modifier=Modifier.weight(1f));Text("Tap to explore",color=muted,fontSize=10.sp)};Row(horizontalArrangement=Arrangement.spacedBy(7.dp)){listOf("Advanced Yoga • 30 days","Martial Arts Mastery","Strength & Conditioning").forEach{title->Card(Modifier.weight(1f).height(40.dp),colors=CardDefaults.cardColors(containerColor=surface)){Box(Modifier.fillMaxSize().blur(2.dp).padding(4.dp),contentAlignment=Alignment.Center){Text(title,color=ink,fontWeight=FontWeight.Bold,fontSize=9.sp,maxLines=2)}}}}}}
@Composable private fun PremiumPaywall(ink:Color,muted:Color,surface:Color,cart:Set<String>,toggle:(String)->Unit,checkout:()->Unit,back:()->Unit){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(11.dp)){TextButton(onClick=back){Icon(Icons.Default.ArrowBack,null);Text(" Back")};Heading("Premium course vault","Advanced training programmes",ink,muted)
 listOf("30-Day Asana Mastery" to "₹899","12-Week Strength Foundations" to "₹1,299","Mobility & Recovery Deep Dive" to "₹699","Handstand Pathway" to "₹799","Nutrition & Meal Planning" to "₹599").forEach{(title,price)->Panel(surface){Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text(title,color=ink,fontWeight=FontWeight.Bold);Text(price+" • Paid course",color=muted,fontSize=11.sp)};Button(onClick={toggle(title)}){Text(if(title in cart)"Remove" else "Add to cart")}}}}
 Button(onClick=checkout,enabled=cart.isNotEmpty(),modifier=Modifier.fillMaxWidth()){Text("Go to cart • "+cart.size+" items")}
 }
}

@Composable private fun Heading(title:String,sub:String,ink:Color,muted:Color){Column(verticalArrangement=Arrangement.spacedBy(3.dp)){Text(title,color=ink,fontSize=23.sp,fontWeight=FontWeight.Bold);Text(sub,color=muted,fontSize=13.sp)}}
@Composable private fun Panel(surface:Color,content:@Composable ColumnScope.()->Unit){Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(containerColor=surface),content=content)}
@Composable private fun HomeScreen(ink:Color,muted:Color,surface:Color,lime:Color,unlocked:Boolean,streak:Int,workouts:Int,recipeCount:Int,heightCm:String,weightKg:String,navigate:(Tab)->Unit){
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(15.dp)){
 item{Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF344A24),Color(0xFF18251B))),RoundedCornerShape(26.dp)).padding(22.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Text("YOUR NEXT REP STARTS HERE",color=lime,fontSize=11.sp,fontWeight=FontWeight.Bold);Text("Build a stronger\nyou.",color=Color.White,fontSize=34.sp,lineHeight=38.sp,fontWeight=FontWeight.ExtraBold);Text("Train smart. Eat well. Show up.",color=Color.White);Button(onClick={navigate(Tab.Train)},colors=ButtonDefaults.buttonColors(containerColor=lime,contentColor=Color.Black)){Text("Explore training");Icon(Icons.Default.ArrowForward,null)}}}
 item{Row(horizontalArrangement=Arrangement.spacedBy(9.dp)){MiniStat("STREAK",streak.toString()+" days","current run",surface,ink,muted,Modifier.weight(1f));MiniStat("WORKOUTS",workouts.toString(),"this week",surface,ink,muted,Modifier.weight(1f));MiniStat("RECIPES",recipeCount.toString(),"in your library",surface,ink,muted,Modifier.weight(1f))}}
 item{Heading("Your training floor","Pick a discipline and get moving.",ink,muted)}
 item{WorkoutPlanner(ink,muted,surface,heightCm,weightKg)}
 item{HabitPamphlet(ink,muted,surface)}
 item{listOf("🥊  Martial arts" to "Technique and discipline","🧘  Yoga" to "Mobility and mindful movement","💪  Calisthenics" to "Bodyweight strength").forEach{p->Panel(surface){Row(Modifier.clickable{navigate(Tab.Train)}.padding(15.dp),verticalAlignment=Alignment.CenterVertically){Text(p.first,color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text(p.second,color=muted,fontSize=11.sp);Icon(Icons.Default.ChevronRight,null,tint=muted)}};Spacer(Modifier.height(8.dp))}}
 item{Text("Your activity and progress are tracked locally on this device.",color=muted,fontSize=11.sp)}
 }
}
@Composable private fun MiniStat(label:String,value:String,caption:String,surface:Color,ink:Color,muted:Color,modifier:Modifier){Card(modifier,colors=CardDefaults.cardColors(containerColor=surface),shape=RoundedCornerShape(16.dp)){Column(Modifier.padding(11.dp)){Text(label,color=muted,fontSize=10.sp,fontWeight=FontWeight.Bold);Text(value,color=ink,fontSize=20.sp,fontWeight=FontWeight.Bold);Text(caption,color=muted,fontSize=10.sp)}}}
@Composable private fun TrainingScreen(lessons:List<Lesson>,ink:Color,muted:Color,surface:Color,lime:Color,cart:Set<String>,toggleCart:(String)->Unit,open:(Lesson)->Unit){
 var category by remember{mutableStateOf("Martial arts")};val selected=when(category){"Martial arts"->lessons.take(4);"Yoga"->lessons.drop(4).take(4);else->lessons.drop(8)}
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){item{Heading("Train your way","Original in-app training programmes for every level",ink,muted)};item{Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){listOf("Martial arts","Yoga","Calisthenics").forEach{FilterChip(selected=category==it,onClick={category=it},label={Text(it)})}}}
 items(selected){l->Card(Modifier.fillMaxWidth().clickable{open(l)},shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(containerColor=surface)){Column{Box(Modifier.fillMaxWidth().height(140.dp).background(Brush.linearGradient(listOf(Color(0xFF4D6651),Color(0xFF242E40)))).padding(16.dp),contentAlignment=Alignment.Center){Column(Modifier.blur(0.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(if(category=="Yoga")"🧘" else if(category=="Martial arts")"🥊" else "💪",fontSize=42.sp);Text("TRAINING SESSION",color=Color.White,fontWeight=FontWeight.Black,letterSpacing=2.sp)};Icon(Icons.Default.PlayCircle,null,tint=Color.White,modifier=Modifier.size(38.dp))}
 Column(Modifier.padding(14.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(l.name,color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text("FREE",color=lime,fontWeight=FontWeight.Bold,fontSize=11.sp)};Text(l.desc,color=muted,fontSize=12.sp);Text("Tap to open course",color=muted,fontSize=11.sp)}
 }}}
 item{AsanaLibrary(ink,muted,surface)};item{YouTubeLibrary(ink,muted,surface,cart,toggleCart)}
 }
}
@Composable private fun FoodScreen(foods:List<Meal>,allergies:Set<String>,setAllergies:(Set<String>)->Unit,ink:Color,muted:Color,surface:Color){
 val choices=listOf("Peanuts","Dairy","Eggs","Gluten","Soy","Sesame","Tree nuts","Fish","Shellfish","Legumes","Seeds","Coconut");val visible=foods.filter{it.allergens.intersect(allergies).isEmpty()}
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){item{Heading("Food that fuels you","Meal ideas, estimated macros and recipes.",ink,muted)}
 item{Panel(surface){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){Text("Allergy filter",color=ink,fontWeight=FontWeight.Bold);Text("Select allergens to hide matching recipes.",color=muted,fontSize=12.sp);choices.chunked(3).forEach{group->Row(horizontalArrangement=Arrangement.spacedBy(4.dp)){group.forEach{a->FilterChip(selected=a in allergies,onClick={setAllergies(if(a in allergies)allergies-a else allergies+a)},label={Text(a,fontSize=10.sp)})}}}}}}
 item{Text(visible.size.toString()+" recipes shown • "+(foods.size-visible.size).toString()+" hidden by allergy filter",color=muted,fontSize=12.sp)}
 items(visible){f->Panel(surface){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){Text(f.name,color=ink,fontSize=17.sp,fontWeight=FontWeight.Bold);Text(f.tag,color=Color(0xFF8BAF52),fontSize=11.sp,fontWeight=FontWeight.Bold);Row(horizontalArrangement=Arrangement.spacedBy(13.dp)){Macro("kcal",f.kcal.toString(),ink,muted);Macro("Protein",f.protein.toString()+"g",ink,muted);Macro("Carbs",f.carbs.toString()+"g",ink,muted);Macro("Fat",f.fat.toString()+"g",ink,muted)};Text("Ingredients",color=ink,fontWeight=FontWeight.SemiBold,fontSize=13.sp);Text(f.ingredients,color=muted,fontSize=12.sp);Text("Method",color=ink,fontWeight=FontWeight.SemiBold,fontSize=13.sp);Text(f.method,color=muted,fontSize=12.sp);if(f.allergens.isNotEmpty())Text("Contains: "+f.allergens.joinToString(),color=Color(0xFFE2A84D),fontSize=11.sp)}}
 }
 item{Text("Nutrition values are approximate per serving. Body-size labels are illustrative, not diagnoses or personalised prescriptions. Consult a registered dietitian for medical needs, pregnancy, eating-disorder history or severe allergies. Verify food labels and cross-contact.",color=muted,fontSize=11.sp)}
 }
}
@Composable private fun Macro(label:String,value:String,ink:Color,muted:Color){Column{Text(value,color=ink,fontWeight=FontWeight.Bold,fontSize=14.sp);Text(label,color=muted,fontSize=10.sp)}}
@Composable private fun Tracker(water:Int,add:()->Unit,remove:()->Unit,meals:Int,addMeal:()->Unit,foodLogs:List<FoodLog>,logFood:(String,Int)->Unit,foodName:String,setFoodName:(String)->Unit,foodKcal:String,setFoodKcal:(String)->Unit,streak:Int,workouts:Int,minutes:Int,logWorkout:()->Unit,ink:Color,muted:Color,surface:Color){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){Heading("Daily check-in","Small consistent actions add up.",ink,muted)
 Panel(surface){Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text("💧 Water tracker",color=ink,fontWeight=FontWeight.Bold,fontSize=19.sp);Text(water.toString()+" / 8 glasses",color=ink,fontSize=28.sp,fontWeight=FontWeight.ExtraBold);LinearProgressIndicator(progress={ (water/8f).coerceIn(0f,1f) },modifier=Modifier.fillMaxWidth());Row{OutlinedButton(onClick=remove){Text("−")};Spacer(Modifier.width(10.dp));Button(onClick=add){Text("+ Add glass")}}}}
 Panel(surface){Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text("🥗 Food & calorie log",color=ink,fontWeight=FontWeight.Bold,fontSize=19.sp);Text(meals.toString()+" meals • "+foodLogs.sumOf{it.kcal}+" kcal entered",color=ink,fontWeight=FontWeight.Bold);OutlinedTextField(value=foodName,onValueChange=setFoodName,label={Text("Food / meal type")},modifier=Modifier.fillMaxWidth(),singleLine=true);OutlinedTextField(value=foodKcal,onValueChange={setFoodKcal(it.filter{c->c.isDigit()})},label={Text("Calories (kcal)")},modifier=Modifier.fillMaxWidth(),singleLine=true);Button(onClick={val n=foodKcal.toIntOrNull();if(foodName.isNotBlank()&&n!=null&&n in 1..10000){logFood(foodName.trim(),n);setFoodName("");setFoodKcal("")}},enabled=foodName.isNotBlank()&&(foodKcal.toIntOrNull()?:0) in 1..10000){Text("Add food entry")};foodLogs.takeLast(8).reversed().forEach{Text("• "+it.name+" — "+it.kcal+" kcal",color=muted,fontSize=12.sp)}}}
 Panel(surface){Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){Text("🏃 Workout & streak tracker",color=ink,fontWeight=FontWeight.Bold,fontSize=18.sp);Text(workouts.toString()+" workouts • "+minutes.toString()+" minutes this week • "+streak.toString()+"-day streak",color=muted,fontSize=12.sp);Button(onClick=logWorkout){Text("Log a 30-minute workout")}}}
 Panel(surface){Column(Modifier.padding(18.dp)){Text("Hydration reminder",color=ink,fontWeight=FontWeight.Bold);Text("Drink regularly and use thirst and your own circumstances as a guide. Hydration needs vary.",color=muted,fontSize=13.sp)}}
 }
}
private fun productImage(name:String):String = when {
 name.contains("mat",true)->"https://www.gaiam.com/cdn/shop/products/05-64061_6MM-GAIAM-ESSENTIALS-YOGA-MAT-TEAL_C_600x.jpg?v=1668557105"
 name.contains("band",true)||name.contains("tube",true)->"https://mediahub.boohoo.com/m5031470265533_green_xl.jpeg"
 name.contains("dumbbell",true)->"https://vitalgym.co/cdn/shop/products/vital-gym-hex-dumbbell-set-140kg-140kg-hex-dumbbell-set-rubber-dumbbells-for-home-gym-vital-gym-41423706128598_1000x.jpg?v=1742042486"
 name.contains("rope",true)->"https://www.tradeinn.com/f/13893/138936719_2/sklz-pro-jump-rope.webp"
 name.contains("roller",true)||name.contains("massage",true)->"https://uk.dfyne.com/cdn/shop/files/BlackRoller_f06f7af2-7c19-418f-9a76-c5b689523136.jpg?v=1742395595&width=1080"
 name.contains("block",true)||name.contains("strap",true)->"https://absolute-snow-content.cdn.rlab.net/original/9e5790e2-db7d-4ea9-bae1-0feca5fdda4405-63680_PRINTED-YOGA-BLOCK-BLUE-SHADOW-POINT_C.jpg"
 name.contains("pull",true)||name.contains("push",true)||name.contains("grip",true)->"https://cdn.sanity.io/images/e3j0flvu/production/16ae3aefc95573e10afb09447a8550148ab90f87-1536x1024.webp?auto=format&q=100&w=1536"
 else->"https://www.soulflex.in/cdn/shop/files/67_4381264e-e6ec-4f0c-a97c-d3ad89eb46ee.png?v=1756986454"
}
private fun productUrl(name:String):String = when {
 name.contains("mat",true)->"https://www.decathlon.in/p/8769263/yoga-mat-6mm-thick-183-x-61-cm-better-grip-with-carry-strap-green"
 name.contains("band",true)||name.contains("tube",true)->"https://www.decathlon.in/shop/decathlon-Fitnessandwellness-Collection"
 name.contains("dumbbell",true)->"https://www.zepto.com/cn/toys-sports/gym-yoga/cid/020d0d76-742f-4474-aad7-01313b65e3cb/scid/ad88206b-9835-4363-abfa-ff1ea5f15272"
 name.contains("rope",true)->"https://www.decathlon.in/shop/decathlon-Fitnessandwellness-Collection"
 name.contains("block",true)||name.contains("strap",true)->"https://www.decathlon.in/shop/decathlon-Fitnessandwellness-Collection"
 name.contains("roller",true)||name.contains("massage",true)->"https://www.decathlon.in/shop/decathlon-Fitnessandwellness-Collection"
 else->"https://www.decathlon.in/shop/decathlon-Fitnessandwellness-Collection"
}
private fun courseSteps(name:String):List<String> = when(name){
 "Boxing foundations"->listOf("Warm-up: shoulder circles, wrist mobility, marching and a light bounce for 3 minutes.","Stance: feet staggered, knees soft, chin tucked and hands protecting cheeks; reset guard after every movement.","Footwork: step-slide forward, back and laterally without crossing feet; 3 rounds of 60 seconds.","Jab-cross: rotate the rear hip on the cross, keep wrists straight and return each hand to guard; 3 × 10 slow combinations.","Cool down with easy walking and gentle calf, chest and forearm stretches.")
 "Muay Thai basics"->listOf("Warm-up: ankle and hip circles, calf raises and 2 minutes of light shadowboxing.","Stance: balanced staggered feet, elbows relaxed and hands high; practise stance resets.","Teep: lift the knee, extend under control, retract and set down; 3 × 8 each leg.","Round kick: pivot the supporting foot and turn the hip through a comfortable range; practise slowly without impact.","Cool down with easy walking and gentle hip-flexor and calf mobility.")
 "Brazilian Jiu-Jitsu"->listOf("Warm-up: hip bridges, shoulder rolls on a mat and controlled hip escapes.","Shrimp escape: plant one foot, lift hips and move diagonally away; alternate sides 3 × 6.","Bridge and turn: plant feet, lift hips and turn onto one shoulder; 2 × 8 slowly.","Technical stand-up: post one hand, plant opposite foot, lift hips and step back to base; 2 × 5 each side.","Solo movement practice only; partner grappling requires a qualified instructor.")
 "Karate fundamentals"->listOf("Warm-up: wrist, ankle, hip and shoulder circles with gentle stance transitions.","Front stance: front knee tracks over toes, rear leg long and hips square; hold 3 × 20 seconds each side.","Straight punch: step forward, extend fist with wrist straight, rotate hips and retract to chamber; 3 × 8.","Downward block: sweep forearm diagonally down while keeping shoulders relaxed; 3 × 8 each side.","Finish with slow kata review and relaxed breathing.")
 "Vinyasa flow"->listOf("Warm-up: cat-cow 6 rounds, tabletop wrist preparation and child's pose.","Flow: mountain → raised arms → forward fold → half lift → plank; repeat 3 slow rounds.","Lower knees before chaturanga if needed; move to cobra and child's pose.","Downward dog: hands shoulder-width, bend knees as needed and lengthen spine; hold 5 breaths.","Finish in supine rest for 1–2 minutes.")
 "Mobility & flexibility"->listOf("Warm-up: 3 minutes easy walking and joint circles.","Ankle rocks: move knee forward over toes while heel stays down; 2 × 10 each side.","90/90 hip switches: sit with knees bent and rotate gently side to side; 2 × 8.","Thoracic open-book: side-lying, rotate upper arm open without forcing lower back; 2 × 8.","Finish with a gentle doorway chest stretch and relaxed breathing.")
 "Restorative yoga"->listOf("Set up folded blankets, cushions or a bolster; keep the room warm and quiet.","Supported child's pose: rest chest and head on support for 1–3 minutes.","Reclined bound angle: support knees with cushions and breathe gently for 2–3 minutes.","Legs-up-the-wall: keep hips a comfortable distance from wall; stay 2–5 minutes if comfortable.","Finish with savasana and exit slowly.")
 "Sun salutations"->listOf("Prepare with neck, shoulder, wrist and ankle movements for 2 minutes.","Mountain: ground feet evenly, lengthen spine and breathe steadily.","Inhale arms up; exhale fold with bent knees; inhale half lift with a long spine.","Step back to plank; lower knees and chest or use cobra; move to downward dog.","Step forward, half lift, fold and rise to mountain. Repeat 2–4 comfortable rounds.")
 "Calisthenics starter"->listOf("Warm-up: brisk marching, arm circles and hip hinges for 3 minutes.","Squat to chair: 2 × 8, knees track over toes and sit back under control.","Incline push-up: hands on a stable counter, body straight; 2 × 6–10.","Glute bridge 2 × 10 and dead bug 2 × 6 each side; rest between sets.","Cool down with easy walking and comfortable stretches.")
 "Pull-up progression"->listOf("Warm shoulders with arm circles, scapular retractions and band pull-aparts.","Dead hang only from a secure rated bar if grip and shoulders tolerate it; 3 × 10 seconds.","Scapular pull: keep elbows straight and draw shoulder blades down; 2 × 6.","Resistance-band rows or secure inverted rows; 3 × 8.","Build gradually with rest days; avoid maximal attempts.")
 "Handstand pathway"->listOf("Warm wrists with gentle palm lifts, finger pulses and shoulder flexion.","Pike hold with feet on floor; push floor away and keep ribs controlled; 3 × 15 seconds.","Practise hollow-body hold and wall-facing shoulder alignment in short sets.","Wall walks require a clear area and a spotter; stop before fatigue.","Inversions require coaching; avoid if you have eye, blood-pressure, neck or balance concerns.")
 else->listOf("Warm-up: 3 minutes easy movement and joint preparation.","Dead bug: lie on back, brace gently and lower opposite arm and leg; 2 × 8 each side.","Bird-dog: from tabletop extend opposite arm and leg without twisting; 2 × 8 each side.","Front plank from knees or hands: 3 × 15–30 seconds with steady breathing.","Cool down with gentle trunk rotation and relaxed breathing.")
}

@Composable private fun Shop(products:List<Triple<String,String,Int>>,cart:Set<String>,toggle:(String)->Unit,ink:Color,muted:Color,surface:Color,checkout:()->Unit){
 val context=LocalContext.current
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){item{Heading("The gear corner","Training equipment, accessories and video courses",ink,muted)}
 item{Panel(surface){Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Text("🛒  "+cart.size+" items in cart",color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Button(onClick=checkout,enabled=cart.isNotEmpty()){Text("Checkout")}}}}
 items(products){p->Panel(surface){Row(Modifier.padding(12.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)){AsyncImage(model=productImage(p.first),contentDescription=p.first,modifier=Modifier.size(88.dp).background(Color.White,RoundedCornerShape(12.dp)),contentScale=ContentScale.Crop);Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(3.dp)){Text(p.first,color=ink,fontWeight=FontWeight.Bold,fontSize=13.sp);Text(p.second,color=ink,fontWeight=FontWeight.Bold);Text("Listed retailer price • ₹"+p.third,color=muted,fontSize=10.sp);TextButton(onClick={context.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(productUrl(p.first))))},contentPadding=PaddingValues(0.dp)){Text("View retailer listing")}};Button(onClick={toggle(p.first)}){Text(if(p.first in cart)"Remove" else "Add")}}}}
 item{Text("Retailer listings open in your browser. Prices and availability can change.",color=muted,fontSize=11.sp)}
 }
}
@Composable private fun Checkout(items:List<Pair<String,Int>>,ink:Color,muted:Color,surface:Color,close:()->Unit){
 Column(Modifier.fillMaxSize().background(Color(0xFF090A0E).copy(.96f)).padding(22.dp),verticalArrangement=Arrangement.Center){Panel(surface){Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){Text("🔒 Checkout",color=ink,fontSize=23.sp,fontWeight=FontWeight.ExtraBold);Text("Your selected items",color=muted,fontSize=13.sp);if(items.isEmpty())Text("Premium course access",color=ink);items.forEach{r->Row{Text(r.first,color=ink,modifier=Modifier.weight(1f));Text("₹"+r.second,color=ink,fontWeight=FontWeight.Bold)}};HorizontalDivider();Row{Text("Total",color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text("₹"+items.sumOf{it.second},color=ink,fontWeight=FontWeight.ExtraBold)};Text("Sorry, user. You are too broke to pay. 🥲",color=Color(0xFFE5A64C),fontWeight=FontWeight.Bold,fontSize=16.sp);Text("Payment is not available for this order.",color=muted,fontSize=12.sp);Button(onClick=close,modifier=Modifier.fillMaxWidth()){Text("Accept my broke status")};TextButton(onClick=close,modifier=Modifier.align(Alignment.CenterHorizontally)){Text("Go back")}}}}
}
@Composable private fun Community(randomPosts:List<CommunityPost>,likes:Set<Int>,toggle:(Int)->Unit,ink:Color,muted:Color,surface:Color){
 val posts=originalPosts+randomPosts
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
 item{Heading("The noticeboard","Five pinned community stories + up to 20 rotating posts.",ink,muted)}
 item{Panel(surface){Column(Modifier.padding(14.dp)){Text("🗣️ Community feed",color=ink,fontWeight=FontWeight.Bold);Text("A new community post appears every 2 minutes 30 seconds while the app is open. Older posts are removed automatically; the five original stories stay pinned.",color=muted,fontSize=12.sp);Text("Pinned: 5   •   Recent posts: "+randomPosts.size+" / 20",color=Color(0xFF8BAF52),fontWeight=FontWeight.Bold,fontSize=11.sp)}}}
 items(posts,key={it.id}){p->Panel(surface){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(p.author,color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));if(p.pinned)Text("📌 PINNED",color=Color(0xFFE2A84D),fontSize=10.sp,fontWeight=FontWeight.Bold);else Text(p.tag,color=muted,fontSize=10.sp)};Text(p.message,color=ink,fontSize=13.sp);Row(verticalAlignment=Alignment.CenterVertically){Text("♥ "+(p.likes+(if(p.id in likes)1 else 0)),color=Color(0xFFE58A9B),fontSize=12.sp);Spacer(Modifier.width(10.dp));TextButton(onClick={toggle(p.id)}){Text(if(p.id in likes)"Liked" else "Like")}}}}}
 }
}
@Composable private fun OnboardingScreen(step:Int,setStep:(Int)->Unit,name:String,setName:(String)->Unit,dob:String,setDob:(String)->Unit,height:String,setHeight:(String)->Unit,weight:String,setWeight:(String)->Unit,level:String,setLevel:(String)->Unit,hobbies:String,setHobbies:(String)->Unit,email:String,setEmail:(String)->Unit,prefs:SharedPreferences,finish:()->Unit,ink:Color,muted:Color,surface:Color){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(22.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
 Text("MOMENTUM",color=ink,fontSize=27.sp,fontWeight=FontWeight.Black)
 Panel(surface){Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
 Text(if(step==0)"Create your profile" else if(step==1)"Connect your account" else "Your training profile",color=ink,fontWeight=FontWeight.Bold,fontSize=20.sp)
 if(step==0){OutlinedTextField(value=name,onValueChange=setName,label={Text("Full name")},modifier=Modifier.fillMaxWidth());OutlinedTextField(value=dob,onValueChange=setDob,label={Text("Date of birth (DD/MM/YYYY)")},modifier=Modifier.fillMaxWidth())}
 if(step==1){Text("Google Drive backup requires the Android app's Google OAuth client configuration. Your profile can be saved on this device in the meantime.",color=muted,fontSize=12.sp);Text("Google account sign-in and Drive backup need an OAuth client registered for this Android package. Once configured, this step will securely connect your Google account.",color=muted,fontSize=12.sp)}
 if(step==2){OutlinedTextField(value=height,onValueChange=setHeight,label={Text("Height (cm)")},modifier=Modifier.fillMaxWidth());OutlinedTextField(value=weight,onValueChange=setWeight,label={Text("Weight (kg)")},modifier=Modifier.fillMaxWidth());Text("Experience level",color=ink,fontWeight=FontWeight.Bold);listOf("Beginner","Intermediate","Advanced").forEach{FilterChip(selected=level==it,onClick={setLevel(it)},label={Text(it)})};OutlinedTextField(value=hobbies,onValueChange=setHobbies,label={Text("Sports, hobbies and goals")},modifier=Modifier.fillMaxWidth(),minLines=2)}
 Button(onClick={if(step<2)setStep(step+1) else {prefs.edit().putString("name",name.trim()).putString("dob",dob.trim()).putString("height",height.trim()).putString("weight",weight.trim()).putString("level",level).putString("hobbies",hobbies.trim()).putString("google_email",email.trim()).putBoolean("profile_complete",true).apply();finish()}},enabled=when(step){0->name.isNotBlank()&&dob.length>=8;2->(height.toFloatOrNull()?:0f)>0&&(weight.toFloatOrNull()?:0f)>0;else->true},modifier=Modifier.fillMaxWidth()){Text(if(step==2)"Save profile" else "Continue")}
 if(step>0)TextButton(onClick={setStep(step-1)}){Text("Back")}
 }}
 }
}
@Composable private fun SettingsScreen(dark:Boolean,darcula:Boolean,name:String,dob:String,height:String,weight:String,level:String,hobbies:String,email:String,setDark:(Boolean)->Unit,setDarcula:(Boolean)->Unit,ink:Color,muted:Color,surface:Color){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){Heading("Settings & profile","Your account and app preferences",ink,muted)
 Panel(surface){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text("👤 "+name,color=ink,fontWeight=FontWeight.Bold,fontSize=20.sp);Text("Date of birth: "+dob,color=muted);Text("Height: "+height+" cm • Weight: "+weight+" kg",color=muted);Text("Experience: "+level,color=muted);Text("Sports & hobbies: "+hobbies,color=muted);Text("Google account: "+(if(email.isBlank())"Not connected" else email),color=muted);Text("Profile is stored on this device. Google Drive backup is not connected until OAuth configuration is supplied.",color=muted,fontSize=11.sp)}}
 Panel(surface){Column(Modifier.padding(16.dp)){Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Dark theme",color=ink,fontWeight=FontWeight.Bold);Text("Switch between light and dark",color=muted,fontSize=12.sp)};Switch(checked=dark,onCheckedChange={setDark(it);if(!it)setDarcula(false)})};HorizontalDivider();Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Darcula / AMOLED",color=ink,fontWeight=FontWeight.Bold);Text("Pure black background",color=muted,fontSize=12.sp)};Switch(checked=darcula,onCheckedChange={setDarcula(it);setDark(true)})}}}
 }
}
@Composable private fun WorkoutPlanner(ink:Color,muted:Color,surface:Color,profileHeight:String,profileWeight:String){
 var height by remember(profileHeight){mutableStateOf(profileHeight)};var weight by remember(profileWeight){mutableStateOf(profileWeight)};var goal by remember{mutableStateOf("Build strength")}
 val h=(height.toFloatOrNull()?:0f)/100f;val w=weight.toFloatOrNull()?:0f;val bmi=if(h>0)w/(h*h) else 0f
 val category=when{h<=0f||w<=0f->"Add valid height and weight in your profile";bmi<18.5f->"Underweight";bmi<25f->"Average range";bmi<30f->"Overweight range";bmi<35f->"Obesity class I range";bmi<40f->"Obesity class II range";else->"Obesity class III range"}
 val routine=when{h<=0f||w<=0f->"Complete your height and weight in Settings to see a BMI-based routine.";bmi<18.5f->"3 days/week • full-body strength, gentle skill work and recovery days. Prioritise adequate meals and gradual progression.";bmi<25f->"4 days/week • 2 strength sessions, 1 conditioning session and 1 mobility/yoga session.";bmi<30f->"4 days/week • 2 low-impact strength sessions, 2 brisk walks/cycling sessions and mobility.";else->"Start with 3 low-impact sessions/week, short walks and supported strength movements. Increase duration gradually and seek clinical guidance if needed."}
 Panel(surface){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text("🧭 Custom workout planner",color=ink,fontWeight=FontWeight.Bold,fontSize=18.sp);Text("Your profile measurements are used to calculate an adult BMI screening estimate.",color=muted,fontSize=12.sp);Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){OutlinedTextField(value=height,onValueChange={height=it},label={Text("Height cm")},modifier=Modifier.weight(1f),singleLine=true);OutlinedTextField(value=weight,onValueChange={weight=it},label={Text("Weight kg")},modifier=Modifier.weight(1f),singleLine=true)};Text("Goal",color=ink,fontWeight=FontWeight.SemiBold);Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){listOf("Build strength","Improve fitness","Mobility").forEach{FilterChip(selected=goal==it,onClick={goal=it},label={Text(it,fontSize=10.sp)})}};Text("Screening category: "+category+" • BMI "+String.format("%.1f",bmi),color=ink,fontWeight=FontWeight.Bold);Text(when(goal){"Mobility"->"Suggested focus: 3–5 gentle mobility sessions/week, comfortable range of motion and rest as needed."; "Improve fitness"->"Suggested focus: mix conversational-pace cardio, beginner strength and recovery.";else->routine},color=muted,fontSize=12.sp);Text("Suggested week: Mon strength • Tue walk/mobility • Wed rest • Thu strength • Fri easy cardio • Sat yoga • Sun rest.",color=muted,fontSize=12.sp);Text("BMI is a rough adult screening measure, not a diagnosis or a complete measure of health. This generic plan is not suitable for children, pregnancy, eating-disorder recovery or medical restrictions. Adjust to ability and seek professional advice when needed.",color=muted,fontSize=10.sp);Text("BMI MOVEMENT LIBRARY",color=ink,fontWeight=FontWeight.Bold,fontSize=15.sp);val moves=when{bmi<18.5f->listOf("Supported squats • 2 × 8","Wall push-ups • 2 × 8","Resistance-band rows • 2 × 10","Bird-dog • 2 × 8 each side","Gentle yoga flow • 10 minutes");bmi<25f->listOf("Bodyweight squats • 3 × 12","Incline push-ups • 3 × 10","Reverse lunges • 2 × 10 each side","Brisk walk intervals • 20 minutes","Sun salutations • 4 rounds");bmi<30f->listOf("Sit-to-stand • 3 × 10","Low step-ups • 2 × 8 each side","Wall push-ups • 3 × 10","Cycling or brisk walk • 15–25 minutes","Supported warrior poses • 5 breaths");else->listOf("Seated marching • 3 × 1 minute","Supported sit-to-stand • 2 × 6","Wall push-ups • 2 × 8","Pool walking or flat walk • 5–10 minutes","Chair yoga and ankle mobility • 8 minutes")};moves.forEach{Text("• "+it,color=muted,fontSize=12.sp)}}}
}
@Composable private fun HabitPamphlet(ink:Color,muted:Color,surface:Color){
 var showPaid by remember{mutableStateOf(false)}
 Panel(surface){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Text("🌱 The little-things pamphlet",color=ink,fontWeight=FontWeight.Bold,fontSize=18.sp);Text("Small habits that support your training.",color=muted,fontSize=12.sp)
 listOf("FREE • Take a 10-minute walk after a meal","FREE • Keep a water bottle where you work","FREE • Add a fruit or vegetable to two meals","FREE • Keep a consistent sleep and wake window","FREE • Warm up gently before training","PAID • Guided habit reset: 14-day checklist","PAID • Recovery and mobility mini-course","PAID • Meal-prep and grocery planning pack").forEach{row->val locked=row.startsWith("PAID");Row(verticalAlignment=Alignment.CenterVertically){Text(if(locked)"🔒" else "✓",color=if(locked)Color(0xFFE5A64C) else Color(0xFF83B85B));Spacer(Modifier.width(7.dp));Text(row.removePrefix("FREE • ").removePrefix("PAID • "),color=ink,fontSize=12.sp,modifier=Modifier.weight(1f));if(locked)Text("PAID",color=Color(0xFFE5A64C),fontSize=10.sp,fontWeight=FontWeight.Bold)}};Text("Tip: choose one small habit for this week instead of changing everything at once.",color=muted,fontSize=11.sp)}}
}

@Composable private fun CourseDetail(lesson:Lesson,ink:Color,muted:Color,surface:Color,back:()->Unit){
 var completed by remember(lesson.name){mutableStateOf(false)}
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){
 TextButton(onClick=back){Icon(Icons.Default.ArrowBack,null);Text(" Back to courses")}
 Panel(surface){Column(Modifier.padding(17.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Text("FREE COURSE",color=Color(0xFF8BAF52),fontWeight=FontWeight.Bold,fontSize=11.sp);Text(lesson.name,color=ink,fontSize=23.sp,fontWeight=FontWeight.ExtraBold);Text(lesson.desc,color=muted,fontSize=13.sp);Text("Course outline",color=ink,fontWeight=FontWeight.Bold);courseSteps(lesson.name).forEachIndexed{i,line->Row(verticalAlignment=Alignment.Top){Text("${i+1}.",color=Color(0xFF8BAF52),fontWeight=FontWeight.Bold);Spacer(Modifier.width(7.dp));Text(line,color=muted,fontSize=13.sp)}};Button(onClick={completed=!completed},modifier=Modifier.fillMaxWidth()){Text(if(completed)"Completed ✓" else "Mark course complete")}}}
 Panel(surface){Column(Modifier.padding(15.dp)){Text("Training safety",color=ink,fontWeight=FontWeight.Bold);Text("Move within a comfortable range, use appropriate space and equipment, and stop if you feel pain, dizziness or unusual shortness of breath. For martial arts drills, avoid hard contact and practise with qualified supervision.",color=muted,fontSize=12.sp)}}
 }
}
