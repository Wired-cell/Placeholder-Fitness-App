# Momentum Fitness

A native Android fitness app built with Kotlin and Jetpack Compose. Training, meal ideas, trackers and community activity run locally on the device; there is no account system or payment processing.

## What's included

- Martial arts, calisthenics and yoga training programmes, plus a searchable library of 40 asanas.
- 40 recipes with estimated calories and macros, ingredient lists, preparation steps and allergen filters.
- Food diary with manual food-name and kcal entry, water tracking, and workout/streak counters that update when workouts are logged.
- BMI screening calculator with movement suggestions for different BMI bands. BMI is a limited adult screening measure, not a diagnosis or personalised medical plan.
- Free YouTube video-guide links, clearly marked FREE. Videos open in YouTube and require an internet connection.
- Locked, blurred premium-course previews displayed across app pages.
- A community feed with five pinned posts and one new generated post every 2 minutes 30 seconds while the app process is active. The newest 20 generated posts are retained.
- Light, dark and Darcula/AMOLED themes, a local promo-code unlock, and a custom adaptive launcher icon.

## Build APK

GitHub Actions builds a debug APK on pushes to main, pull requests and manual dispatch.

1. Open **Actions** and select **Build Android APK**.
2. Run the workflow or push a commit to main.
3. Download the debug APK artifact from the completed run.

The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Notes

Nutrition values are approximate per-serving estimates, not medical advice. Allergy filtering is a convenience only; independently verify labels and cross-contact. Exercise suggestions are general educational examples; choose movements appropriate to your ability and seek professional guidance for health concerns. YouTube availability, ads and sign-in requirements are controlled by YouTube. Course previews, store prices and offers are illustrative; no payment is collected.

Nutrition guidance is informed by the ICMR–National Institute of Nutrition's *Dietary Guidelines for Indians (2024)* and NHS balanced-diet guidance. Recipe macro figures are illustrative estimates, not official nutrient analyses or individual calorie prescriptions.

- ICMR–NIN: https://www.nin.res.in/dietaryguidelines/pdfjs/locale/DGI24thJune2024fin.pdf
- NHS balanced diet: https://www.nhs.uk/Live-well/eat-well/how-to-eat-a-balanced-diet/eating-a-balanced-diet/
