# Momentum Fitness

Momentum Fitness is a Kotlin and Jetpack Compose Android app for training, yoga, nutrition, activity tracking, community and fitness equipment.

## App features

- First-launch profile setup for name, date of birth, height, weight, experience level and sports or hobbies. Profile details appear in Settings and are retained on the device.
- Dark, light and AMOLED themes, with AMOLED enabled by default.
- Guided boxing, Muay Thai, Brazilian jiu-jitsu, karate, yoga and calisthenics sessions.
- Searchable 40-pose asana library with step-by-step practice instructions.
- Forty recipes with estimated calories, macros, ingredients, preparation steps and allergen filters.
- Food diary with meal-name and kcal entry, water tracking, workout logging and streak counters.
- Adult BMI screening estimate and movement suggestions. BMI is a limited screening measure, not a diagnosis or a personalised medical plan.
- Free YouTube video guides first, followed by paid video courses with add-to-cart controls.
- Fitness equipment shop with product photography, retailer links, cart and checkout screen.
- Premium course collection with paid course options and cart support.
- Community feed with five pinned posts and a new post every 2 minutes 30 seconds while the app process is active. The latest 20 rotating posts are retained.
- Custom adaptive launcher artwork.

## Google account and Drive backup

Profile setup and display are implemented. Google sign-in and Google Drive backup still require an OAuth client registered for the Android package `com.wiredcell.momentumfitness`, plus the matching signing-certificate SHA-1. Once the OAuth client details are supplied, Google Identity sign-in and Drive file sync can be wired to the profile. Until then, profile information is stored in the app's private local preferences; it is not uploaded to Drive.

## Build APK

GitHub Actions builds a debug APK on pushes to the default branch, pull requests and manual dispatch.

1. Open **Actions** and select **Build Android APK**.
2. Run the workflow or push a commit to the default branch.
3. Download the debug APK artifact from the completed run.

The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Store and checkout

The store shows product listings and retailer links. Cart checkout displays the selected items and the requested broke-user message; it does not submit orders or process payments. Retailer prices and availability can change, so confirm the live listing before purchasing.

## Sources

Exercise guidance and yoga techniques are based on public health and official yoga resources:

- WHO adult physical activity recommendations: https://www.who.int/news-room/fact-sheets/detail/physical-activity
- Ministry of AYUSH Common Yoga Protocol: https://yoga.ayush.gov.in/YAP/PDF/Common%20Yoga%20Protocol%20Book-English.pdf
- Ministry of AYUSH yoga posture guidance: https://www.mea.gov.in/yoga-postures-17.htm
- American Heart Association resistance-training guidance: https://www.heart.org/en/healthy-living/exercise-and-physical-activity/fitness-basics/strength-and-resistance-training-exercise

Product photos and retailer listings are linked from their respective publishers and sellers. Example sources include Decathlon, Gaiam, Boldfit, and fitness-equipment retailers. Product prices are indicative and may change.

Nutrition values are approximate per-serving estimates, not medical advice. Allergy filtering is a convenience only; independently verify product labels and cross-contact. Exercise suggestions are general education; adapt to ability and seek qualified guidance for health concerns.
