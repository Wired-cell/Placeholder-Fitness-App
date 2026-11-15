package com.wiredcell.placeholderfitness
import android.os.Bundle
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
import kotlin.random.Random
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { FitnessApp() } } }
private enum class Tab(val title:String,val icon:androidx.compose.ui.graphics.vector.ImageVector) {
 Home("Home",Icons.Default.Home), Train("Train",Icons.Default.FitnessCenter), Food("Food",Icons.Default.Restaurant), Track("Track",Icons.Default.WaterDrop), Shop("Shop",Icons.Default.ShoppingBag), Connect("Connect",Icons.Default.Groups)
}
private data class Lesson(val name:String,val desc:String,val query:String,val paid:Boolean)
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
private data class Meal(val name:String,val tag:String,val kcal:Int,val protein:Int,val carbs:Int,val fat:Int,val ingredients:String,val method:String,val allergens:Set<String>)
@OptIn(ExperimentalMaterial3Api::class)
@Composable private fun FitnessApp() {
 var tab by remember { mutableStateOf(Tab.Home) }; var dark by remember { mutableStateOf(true) }; var darcula by remember { mutableStateOf(false) }
 var unlocked by remember { mutableStateOf(false) }; var settings by remember { mutableStateOf(false) }; var selectedLesson by remember { mutableStateOf<Lesson?>(null) }
 var randomPosts by remember { mutableStateOf(emptyList<CommunityPost>()) }; var postCounter by remember { mutableIntStateOf(100) }
 LaunchedEffect(Unit) { while(true) { delay(300_000L); randomPosts=(listOf(makeRandomPost(postCounter))+randomPosts).take(20); postCounter++ } }
 var gateway by remember { mutableStateOf(false) }; var gatewayItems by remember { mutableStateOf(listOf<Pair<String,Int>>()) }
 var promo by remember { mutableStateOf("") }; var promoMsg by remember { mutableStateOf("") }; var water by remember { mutableIntStateOf(3) }; var mealCount by remember { mutableIntStateOf(2) }
 var allergies by remember { mutableStateOf(setOf<String>()) }; var cart by remember { mutableStateOf(setOf<String>()) }; var likes by remember { mutableStateOf(setOf<Int>()) }
 val bg=if(!dark) Color(0xFFF4F5F7) else if(darcula) Color(0xFF211F26) else Color(0xFF101116)
 val surface=if(!dark) Color.White else if(darcula) Color(0xFF302D36) else Color(0xFF1C1E27)
 val ink=if(dark) Color(0xFFF4F2FA) else Color(0xFF171923); val muted=if(dark) Color(0xFFB8B5C4) else Color(0xFF656978); val lime=Color(0xFFB9F36A)
 val lessons=listOf(
 Lesson("Boxing foundations","Footwork, guard and jab-cross • 18 min","beginner boxing footwork guard jab cross tutorial",false),
 Lesson("Muay Thai basics","Stance, teep and roundhouse • 24 min","",false),
 Lesson("Brazilian Jiu-Jitsu","Escapes and positional basics • 21 min","",false),
 Lesson("Karate fundamentals","Kihon and controlled combinations • 16 min","karate kihon beginner fundamentals tutorial",false),
 Lesson("Vinyasa flow","A steady full-body sequence • 22 min","beginner vinyasa yoga full body flow",false),
 Lesson("Mobility & flexibility","Hips, shoulders and spine • 15 min","",false),
 Lesson("Restorative yoga","Gentle evening recovery • 20 min","restorative yoga beginner evening routine",false),
 Lesson("Sun salutations","A guided Surya Namaskar • 12 min","surya namaskar sun salutations guided beginner",false),
 Lesson("Calisthenics starter","Push, pull and squat patterns • 19 min","calisthenics beginner full body workout tutorial",false),
 Lesson("Pull-up progression","Build toward your first pull-up • 17 min","",false),
 Lesson("Handstand pathway","Wrist prep and wall drills • 14 min","",false),
 Lesson("Core control","Hollow body and plank variations • 13 min","calisthenics beginner core hollow body plank",false))
 val foods=listOf(
 Meal("Peanut-banana oats","Underweight • energy dense",520,20,72,18,"Oats 60g, milk 200ml, banana, peanut butter 1 tbsp, chia 1 tsp","Simmer oats in milk. Top with banana, peanut butter and chia.",setOf("Peanuts","Dairy")),
 Meal("Paneer rice bowl","Underweight • vegetarian",610,27,78,20,"Rice 1 cup, paneer 100g, peas ½ cup, curd ½ cup","Pan-sear paneer with spices. Serve over rice with peas and curd.",setOf("Dairy")),
 Meal("Moong dal khichdi","Average • balanced",430,18,66,10,"Rice ½ cup, moong dal ½ cup, carrot, peas, cumin, oil 1 tsp","Rinse rice and dal. Pressure-cook with vegetables, cumin and water.",emptySet()),
 Meal("Chickpea quinoa salad","Average • active day",490,21,69,15,"Quinoa ¾ cup, chickpeas ¾ cup, cucumber, tomato, lemon, olive oil","Combine cooked quinoa and chickpeas with vegetables; dress with lemon.",emptySet()),
 Meal("Grilled tofu millet plate","Overweight • fibre forward",440,25,52,14,"Tofu 150g, cooked millet ¾ cup, mixed vegetables 2 cups","Grill seasoned tofu. Serve with millet and steamed vegetables.",setOf("Soy")),
 Meal("Dal & vegetable roti","Obesity class I • portion aware",460,22,61,13,"Masoor dal 1 cup, 2 small whole-wheat rotis, vegetables, salad","Cook dal with tomato and spices. Serve with rotis and salad.",setOf("Gluten")),
 Meal("Egg bhurji & roti","Obesity class II • protein rich",410,26,38,15,"2 eggs, onion, tomato, spinach, 1 small roti","Scramble eggs with vegetables. Serve with roti and cucumber.",setOf("Eggs","Gluten")),
 Meal("Lentil & sweet potato","All body types • plant based",475,19,76,11,"Lentils 1 cup, sweet potato 150g, spinach, tahini 1 tsp","Roast sweet potato. Add warm lentils and spinach; finish with tahini.",setOf("Sesame")))
 val products=listOf(Triple("Resistance band set","₹399",699),Triple("Yoga mat • 6 mm","₹699",1199),Triple("Doorway pull-up bar","₹1,299",1899),Triple("Skipping rope","₹249",499),Triple("Adjustable dumbbells","₹2,499",3499),Triple("Foam roller","₹599",999))
 MaterialTheme(colorScheme=if(dark) darkColorScheme(primary=lime,background=bg,surface=surface,onBackground=ink,onSurface=ink) else lightColorScheme(primary=Color(0xFF426B18),background=bg,surface=surface,onBackground=ink,onSurface=ink)) {
 Scaffold(containerColor=bg,topBar={ TopAppBar(title={Text(if(settings) "Settings" else if(selectedLesson!=null) selectedLesson!!.name else "PLACEHOLDER",fontWeight=FontWeight.Black,letterSpacing=1.2.sp)},actions={IconButton(onClick={settings=!settings;video=""}){Icon(if(settings) Icons.Default.Close else Icons.Default.Settings,"Settings")}},colors=TopAppBarDefaults.topAppBarColors(containerColor=bg,titleContentColor=ink,actionIconContentColor=ink)) },
 bottomBar={if(!settings && selectedLesson==null && !gateway) NavigationBar(containerColor=surface){Tab.entries.forEach{t->NavigationBarItem(selected=tab==t,onClick={tab=t},icon={Icon(t.icon,t.title)},label={Text(t.title,fontSize=10.sp)})}}}) { pad ->
 Box(Modifier.fillMaxSize().padding(pad)) {
 when {
 selectedLesson != null -> CourseDetail(selectedLesson!!,ink,muted,surface,{selectedLesson=null})
 settings -> SettingsScreen(dark,darcula,promo,promoMsg,{dark=it},{darcula=it;dark=true},{promo=it},{if(promo.trim()=="Notiboi Piyush"){unlocked=true;promoMsg="🎉 All paid courses unlocked! Premium martial arts, yoga and calisthenics are now available."}else promoMsg="Code not recognised. Try again."},ink,muted,surface)
 gateway -> Checkout(gatewayItems,ink,muted,surface,{gateway=false})
 else -> when(tab) {
 Tab.Home -> HomeScreen(ink,muted,surface,lime,unlocked,{tab=it})
 Tab.Train -> TrainingScreen(lessons,ink,muted,surface,lime,{l->selectedLesson=l})
 Tab.Food -> FoodScreen(foods,allergies,{allergies=it},ink,muted,surface)
 Tab.Track -> Tracker(water,{water=(water+1).coerceAtMost(20)},{water=(water-1).coerceAtLeast(0)},mealCount,{mealCount++},ink,muted,surface)
 Tab.Shop -> Shop(products,cart,{cart=if(it in cart)cart-it else cart+it},ink,muted,surface,{gatewayItems=cart.mapNotNull{n->products.find{p->p.first==n}?.let{it.first to it.third}};gateway=true})
 Tab.Connect -> Community(randomPosts,likes,{i->likes=if(i in likes)likes-i else likes+i},ink,muted,surface)
 }
 }
 }
 }
}
@Composable private fun Heading(title:String,sub:String,ink:Color,muted:Color){Column(verticalArrangement=Arrangement.spacedBy(3.dp)){Text(title,color=ink,fontSize=23.sp,fontWeight=FontWeight.Bold);Text(sub,color=muted,fontSize=13.sp)}}
@Composable private fun Panel(surface:Color,content:@Composable ColumnScope.()->Unit){Card(Modifier.fillMaxWidth(),shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(containerColor=surface),content=content)}
@Composable private fun HomeScreen(ink:Color,muted:Color,surface:Color,lime:Color,unlocked:Boolean,navigate:(Tab)->Unit){
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(15.dp)){
 item{Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFF344A24),Color(0xFF18251B))),RoundedCornerShape(26.dp)).padding(22.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Text("YOUR NEXT REP STARTS HERE",color=lime,fontSize=11.sp,fontWeight=FontWeight.Bold);Text("Build a stronger\nyou.",color=Color.White,fontSize=34.sp,lineHeight=38.sp,fontWeight=FontWeight.ExtraBold);Text("Train smart. Eat well. Show up.",color=Color.White);Button(onClick={navigate(Tab.Train)},colors=ButtonDefaults.buttonColors(containerColor=lime,contentColor=Color.Black)){Text("Explore training");Icon(Icons.Default.ArrowForward,null)}}}
 item{Row(horizontalArrangement=Arrangement.spacedBy(9.dp)){MiniStat("TODAY","3 / 8","glasses",surface,ink,muted,Modifier.weight(1f));MiniStat("STREAK","4 days","keep going",surface,ink,muted,Modifier.weight(1f));MiniStat("COURSES",if(unlocked)"All" else "4","free lessons",surface,ink,muted,Modifier.weight(1f))}}
 item{Heading("Your training floor","Pick a discipline and get moving.",ink,muted)}
 item{WorkoutPlanner(ink,muted,surface)}
 item{HabitPamphlet(ink,muted,surface)}
 item{listOf("🥊  Martial arts" to "Technique and discipline","🧘  Yoga" to "Mobility and mindful movement","💪  Calisthenics" to "Bodyweight strength").forEach{p->Panel(surface){Row(Modifier.clickable{navigate(Tab.Train)}.padding(15.dp),verticalAlignment=Alignment.CenterVertically){Text(p.first,color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text(p.second,color=muted,fontSize=11.sp);Icon(Icons.Default.ChevronRight,null,tint=muted)}};Spacer(Modifier.height(8.dp))}}
 item{Panel(surface){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){Text("🎁 Welcome offer",color=ink,fontWeight=FontWeight.Bold,fontSize=18.sp);Text("SIGN-UP SPECIAL: "+listOf(15,20,25,30,35).random()+"% OFF selected demo gear today.",color=muted);Text("Fake offer • no purchase required",color=muted,fontSize=11.sp)}}}
 item{Panel(surface){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(5.dp)){Text("🎂 Birthday treat",color=ink,fontWeight=FontWeight.Bold);Text("Birthday month bonus: "+listOf(20,25,30,40,50).random()+"% OFF a fictional premium bundle. Demo offer only; no profile or date of birth is collected.",color=muted,fontSize=12.sp)}}}
 item{Text("Placeholder demo • sample activity and offers are local only.",color=muted,fontSize=11.sp)}
 }
}
@Composable private fun MiniStat(label:String,value:String,caption:String,surface:Color,ink:Color,muted:Color,modifier:Modifier){Card(modifier,colors=CardDefaults.cardColors(containerColor=surface),shape=RoundedCornerShape(16.dp)){Column(Modifier.padding(11.dp)){Text(label,color=muted,fontSize=10.sp,fontWeight=FontWeight.Bold);Text(value,color=ink,fontSize=20.sp,fontWeight=FontWeight.Bold);Text(caption,color=muted,fontSize=10.sp)}}}
@Composable private fun TrainingScreen(lessons:List<Lesson>,ink:Color,muted:Color,surface:Color,lime:Color,open:(Lesson)->Unit){
 var category by remember{mutableStateOf("Martial arts")};val selected=when(category){"Martial arts"->lessons.take(4);"Yoga"->lessons.drop(4).take(4);else->lessons.drop(8)}
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(13.dp)){item{Heading("Train your way","Curated lesson searches • demo catalogue",ink,muted)};item{Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){listOf("Martial arts","Yoga","Calisthenics").forEach{FilterChip(selected=category==it,onClick={category=it},label={Text(it)})}}}
 items(selected){l->Card(Modifier.fillMaxWidth().clickable{open(l)},shape=RoundedCornerShape(20.dp),colors=CardDefaults.cardColors(containerColor=surface)){Column{Box(Modifier.fillMaxWidth().height(140.dp).background(Brush.linearGradient(listOf(Color(0xFF4D6651),Color(0xFF242E40)))).padding(16.dp),contentAlignment=Alignment.Center){Column(Modifier.blur(0.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(if(category=="Yoga")"🧘" else if(category=="Martial arts")"🥊" else "💪",fontSize=42.sp);Text("TRAINING SESSION",color=Color.White,fontWeight=FontWeight.Black,letterSpacing=2.sp)};Icon(Icons.Default.PlayCircle,null,tint=Color.White,modifier=Modifier.size(38.dp))}
 Column(Modifier.padding(14.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(l.name,color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text("FREE",color=lime,fontWeight=FontWeight.Bold,fontSize=11.sp)};Text(l.desc,color=muted,fontSize=12.sp);Text("Tap to open course",color=muted,fontSize=11.sp)}
 }}}
 item{Text("All lessons are original hard-coded course outlines. No external video service is used.",color=muted,fontSize=11.sp)}
 }
}
@Composable private fun FoodScreen(foods:List<Meal>,allergies:Set<String>,setAllergies:(Set<String>)->Unit,ink:Color,muted:Color,surface:Color){
 val choices=listOf("Peanuts","Dairy","Eggs","Gluten","Soy","Sesame","Tree nuts","Fish","Shellfish");val visible=foods.filter{it.allergens.intersect(allergies).isEmpty()}
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){item{Heading("Food that fuels you","Meal ideas, estimated macros and recipes.",ink,muted)}
 item{Panel(surface){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){Text("Allergy filter",color=ink,fontWeight=FontWeight.Bold);Text("Select allergens to hide matching sample recipes.",color=muted,fontSize=12.sp);choices.chunked(3).forEach{group->Row(horizontalArrangement=Arrangement.spacedBy(4.dp)){group.forEach{a->FilterChip(selected=a in allergies,onClick={setAllergies(if(a in allergies)allergies-a else allergies+a)},label={Text(a,fontSize=10.sp)})}}}}}}
 item{Text(visible.size.toString()+" recipes shown • "+(foods.size-visible.size).toString()+" hidden by allergy filter",color=muted,fontSize=12.sp)}
 items(visible){f->Panel(surface){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){Text(f.name,color=ink,fontSize=17.sp,fontWeight=FontWeight.Bold);Text(f.tag,color=Color(0xFF8BAF52),fontSize=11.sp,fontWeight=FontWeight.Bold);Row(horizontalArrangement=Arrangement.spacedBy(13.dp)){Macro("kcal",f.kcal.toString(),ink,muted);Macro("Protein",f.protein.toString()+"g",ink,muted);Macro("Carbs",f.carbs.toString()+"g",ink,muted);Macro("Fat",f.fat.toString()+"g",ink,muted)};Text("Ingredients",color=ink,fontWeight=FontWeight.SemiBold,fontSize=13.sp);Text(f.ingredients,color=muted,fontSize=12.sp);Text("Method",color=ink,fontWeight=FontWeight.SemiBold,fontSize=13.sp);Text(f.method,color=muted,fontSize=12.sp);if(f.allergens.isNotEmpty())Text("Contains: "+f.allergens.joinToString(),color=Color(0xFFE2A84D),fontSize=11.sp)}}
 }
 item{Text("Nutrition values are approximate per serving. Body-size labels are illustrative, not diagnoses or personalised prescriptions. Consult a registered dietitian for medical needs, pregnancy, eating-disorder history or severe allergies. Verify food labels and cross-contact.",color=muted,fontSize=11.sp)}
 }
}
@Composable private fun Macro(label:String,value:String,ink:Color,muted:Color){Column{Text(value,color=ink,fontWeight=FontWeight.Bold,fontSize=14.sp);Text(label,color=muted,fontSize=10.sp)}}
@Composable private fun Tracker(water:Int,add:()->Unit,remove:()->Unit,meals:Int,addMeal:()->Unit,ink:Color,muted:Color,surface:Color){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){Heading("Daily check-in","Small consistent actions add up.",ink,muted)
 Panel(surface){Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text("💧 Water tracker",color=ink,fontWeight=FontWeight.Bold,fontSize=19.sp);Text(water.toString()+" / 8 glasses",color=ink,fontSize=28.sp,fontWeight=FontWeight.ExtraBold);LinearProgressIndicator(progress={ (water/8f).coerceIn(0f,1f) },modifier=Modifier.fillMaxWidth());Row{OutlinedButton(onClick=remove){Text("−")};Spacer(Modifier.width(10.dp));Button(onClick=add){Text("+ Add glass")}}}}
 Panel(surface){Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Text("🥗 Meals logged",color=ink,fontWeight=FontWeight.Bold,fontSize=19.sp);Text(meals.toString()+" meals today",color=ink,fontSize=26.sp,fontWeight=FontWeight.ExtraBold);Button(onClick=addMeal){Text("Log a meal")}}}
 Panel(surface){Column(Modifier.padding(18.dp)){Text("Hydration reminder",color=ink,fontWeight=FontWeight.Bold);Text("Drink regularly and use thirst and your own circumstances as a guide. Hydration needs vary.",color=muted,fontSize=13.sp)}}
 }
}
@Composable private fun Shop(products:List<Triple<String,String,Int>>,cart:Set<String>,toggle:(String)->Unit,ink:Color,muted:Color,surface:Color,checkout:()->Unit){
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){item{Heading("The gear corner","Demo storefront • pretend discounts.",ink,muted)}
 item{Panel(surface){Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Text("🛒  "+cart.size+" items in cart",color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Button(onClick=checkout,enabled=cart.isNotEmpty()){Text("Checkout")}}}}
 items(products){p->Panel(surface){Row(Modifier.padding(13.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)){Box(Modifier.size(66.dp).background(Brush.linearGradient(listOf(Color(0xFF546E50),Color(0xFF263448))),RoundedCornerShape(14.dp)),contentAlignment=Alignment.Center){Text("🏋️",fontSize=28.sp)};Column(Modifier.weight(1f)){Text(p.first,color=ink,fontWeight=FontWeight.Bold);Text(p.second,color=ink,fontWeight=FontWeight.Bold);Text("₹"+p.third+" list • "+Random.nextInt(10,41).toString()+"% demo deal",color=muted,fontSize=11.sp)};Button(onClick={toggle(p.first)}){Text(if(p.first in cart)"Remove" else "Add")}}}}
 item{Text("All prices, stock, discounts and checkout are fictional. No order or payment is made.",color=muted,fontSize=11.sp)}
 }
}
@Composable private fun Checkout(items:List<Pair<String,Int>>,ink:Color,muted:Color,surface:Color,close:()->Unit){
 Column(Modifier.fillMaxSize().background(Color(0xFF090A0E).copy(.96f)).padding(22.dp),verticalArrangement=Arrangement.Center){Panel(surface){Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){Text("🔒 Demo checkout",color=ink,fontSize=23.sp,fontWeight=FontWeight.ExtraBold);Text("Your selected items",color=muted,fontSize=13.sp);if(items.isEmpty())Text("Premium demo access",color=ink);items.forEach{r->Row{Text(r.first,color=ink,modifier=Modifier.weight(1f));Text("₹"+r.second,color=ink,fontWeight=FontWeight.Bold)}};HorizontalDivider();Row{Text("Total",color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));Text("₹"+items.sumOf{it.second},color=ink,fontWeight=FontWeight.ExtraBold)};Text("Sorry, user. You are too broke to pay. 🥲",color=Color(0xFFE5A64C),fontWeight=FontWeight.Bold,fontSize=16.sp);Text("Fictional gateway only. No payment details are collected.",color=muted,fontSize=12.sp);Button(onClick=close,modifier=Modifier.fillMaxWidth()){Text("Accept my broke status")};TextButton(onClick=close,modifier=Modifier.align(Alignment.CenterHorizontally)){Text("Go back")}}}}
}
@Composable private fun Community(randomPosts:List<CommunityPost>,likes:Set<Int>,toggle:(Int)->Unit,ink:Color,muted:Color,surface:Color){
 val posts=originalPosts+randomPosts
 LazyColumn(contentPadding=PaddingValues(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
 item{Heading("The noticeboard","Five pinned community stories + up to 20 rotating posts.",ink,muted)}
 item{Panel(surface){Column(Modifier.padding(14.dp)){Text("🗣️ Community feed",color=ink,fontWeight=FontWeight.Bold);Text("A new fictional post appears every five minutes while the app is open. Older generated posts are removed automatically; the five original stories stay pinned.",color=muted,fontSize=12.sp);Text("Pinned: 5   •   Rotating: "+randomPosts.size+" / 20",color=Color(0xFF8BAF52),fontWeight=FontWeight.Bold,fontSize=11.sp)}}}
 items(posts,key={it.id}){p->Panel(surface){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Row(verticalAlignment=Alignment.CenterVertically){Text(p.author,color=ink,fontWeight=FontWeight.Bold,modifier=Modifier.weight(1f));if(p.pinned)Text("📌 PINNED",color=Color(0xFFE2A84D),fontSize=10.sp,fontWeight=FontWeight.Bold);else Text(p.tag,color=muted,fontSize=10.sp)};Text(p.message,color=ink,fontSize=13.sp);Row(verticalAlignment=Alignment.CenterVertically){Text("♥ "+(p.likes+(if(p.id in likes)1 else 0)),color=Color(0xFFE58A9B),fontSize=12.sp);Spacer(Modifier.width(10.dp));TextButton(onClick={toggle(p.id)}){Text(if(p.id in likes)"Liked" else "Like")}}}}}
 }
}
@Composable private fun SettingsScreen(dark:Boolean,darcula:Boolean,promo:String,message:String,setDark:(Boolean)->Unit,setDarcula:(Boolean)->Unit,setPromo:(String)->Unit,redeem:()->Unit,ink:Color,muted:Color,surface:Color){
 Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){Heading("Preferences","Make the app feel like yours.",ink,muted)
 Panel(surface){Column(Modifier.padding(16.dp)){Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Dark theme",color=ink,fontWeight=FontWeight.Bold);Text("Switch between light and dark",color=muted,fontSize=12.sp)};Switch(checked=dark,onCheckedChange=setDark)};HorizontalDivider();Row(verticalAlignment=Alignment.CenterVertically){Column(Modifier.weight(1f)){Text("Darcula / AMOLED",color=ink,fontWeight=FontWeight.Bold);Text("Use deeper charcoal surfaces",color=muted,fontSize=12.sp)};Switch(checked=darcula,onCheckedChange=setDarcula,enabled=dark)}}}
 Panel(surface){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Text("🎟️ Promo code",color=ink,fontWeight=FontWeight.Bold,fontSize=18.sp);Text("Enter the demo code to unlock premium lessons locally.",color=muted,fontSize=12.sp);OutlinedTextField(value=promo,onValueChange=setPromo,label={Text("Enter promo code")},modifier=Modifier.fillMaxWidth(),singleLine=true);Button(onClick=redeem,modifier=Modifier.fillMaxWidth()){Text("Redeem code")};if(message.isNotBlank())Text(message,color=if(message.startsWith("🎉"))Color(0xFF82B95A) else Color(0xFFE5A64C),fontSize=12.sp)}}
 Panel(surface){Column(Modifier.padding(16.dp)){Text("About this build",color=ink,fontWeight=FontWeight.Bold);Text("Placeholder Fitness v1.0 • hard-coded demo content • no accounts, server, payment processing or personal data upload.",color=muted,fontSize=12.sp)}}
 }
}

@Composable private fun WorkoutPlanner(ink:Color,muted:Color,surface:Color){
 var height by remember{mutableStateOf("170")};var weight by remember{mutableStateOf("68")};var goal by remember{mutableStateOf("Build strength")}
 val h=(height.toFloatOrNull()?:170f)/100f;val w=weight.toFloatOrNull()?:68f;val bmi=if(h>0)w/(h*h) else 0f
 val category=when{bmi<18.5f->"Underweight";bmi<25f->"Average range";bmi<30f->"Overweight range";bmi<35f->"Obesity class I range";bmi<40f->"Obesity class II range";else->"Obesity class III range"}
 val routine=when{bmi<18.5f->"3 days/week • full-body strength, gentle skill work and recovery days. Prioritise adequate meals and gradual progression.";bmi<25f->"4 days/week • 2 strength sessions, 1 conditioning session and 1 mobility/yoga session.";bmi<30f->"4 days/week • 2 low-impact strength sessions, 2 brisk walks/cycling sessions and mobility.";else->"Start with 3 low-impact sessions/week, short walks and supported strength movements. Increase duration gradually and seek clinical guidance if needed."}
 Panel(surface){Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text("🧭 Custom workout planner",color=ink,fontWeight=FontWeight.Bold,fontSize=18.sp);Text("Enter sample measurements to generate a hard-coded starter routine.",color=muted,fontSize=12.sp);Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){OutlinedTextField(value=height,onValueChange={height=it},label={Text("Height cm")},modifier=Modifier.weight(1f),singleLine=true);OutlinedTextField(value=weight,onValueChange={weight=it},label={Text("Weight kg")},modifier=Modifier.weight(1f),singleLine=true)};Text("Goal",color=ink,fontWeight=FontWeight.SemiBold);Row(horizontalArrangement=Arrangement.spacedBy(5.dp)){listOf("Build strength","Improve fitness","Mobility").forEach{FilterChip(selected=goal==it,onClick={goal=it},label={Text(it,fontSize=10.sp)})}};Text("Screening category: "+category+" • BMI "+String.format("%.1f",bmi),color=ink,fontWeight=FontWeight.Bold);Text(when(goal){"Mobility"->"Suggested focus: 3–5 gentle mobility sessions/week, comfortable range of motion and rest as needed."; "Improve fitness"->"Suggested focus: mix conversational-pace cardio, beginner strength and recovery.";else->routine},color=muted,fontSize=12.sp);Text("Example week: Mon strength • Tue walk/mobility • Wed rest • Thu strength • Fri easy cardio • Sat yoga • Sun rest.",color=muted,fontSize=12.sp);Text("BMI is a rough adult screening measure, not a diagnosis or a complete measure of health. This generic plan is not suitable for children, pregnancy, eating-disorder recovery or medical restrictions. Adjust to ability and seek professional advice when needed.",color=muted,fontSize=10.sp)}}
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
 Panel(surface){Column(Modifier.padding(17.dp),verticalArrangement=Arrangement.spacedBy(9.dp)){Text("FREE COURSE",color=Color(0xFF8BAF52),fontWeight=FontWeight.Bold,fontSize=11.sp);Text(lesson.name,color=ink,fontSize=23.sp,fontWeight=FontWeight.ExtraBold);Text(lesson.desc,color=muted,fontSize=13.sp);Text("Course outline",color=ink,fontWeight=FontWeight.Bold);listOf("Warm-up • 3–5 minutes of easy movement and joint preparation.","Technique • Learn the fundamental position, breathing and controlled movement.","Practice • Repeat 3 rounds at a comfortable pace, resting between rounds.","Cool-down • Slow down, breathe normally and stretch gently if comfortable.").forEachIndexed{i,line->Row(verticalAlignment=Alignment.Top){Text("${i+1}.",color=Color(0xFF8BAF52),fontWeight=FontWeight.Bold);Spacer(Modifier.width(7.dp));Text(line,color=muted,fontSize=13.sp)}};Button(onClick={completed=!completed},modifier=Modifier.fillMaxWidth()){Text(if(completed)"Completed ✓" else "Mark course complete")}}}
 Panel(surface){Column(Modifier.padding(15.dp)){Text("Training safety",color=ink,fontWeight=FontWeight.Bold);Text("Move within a comfortable range, use appropriate space and equipment, and stop if you feel pain, dizziness or unusual shortness of breath. For martial arts drills, avoid hard contact and practise with qualified supervision.",color=muted,fontSize=12.sp)}}
 }
}
