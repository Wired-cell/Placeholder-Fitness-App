# Placeholder Fitness App

Native Android fitness demo built with Kotlin and Jetpack Compose. Hard-coded sample content and local UI state; no backend, account system, API keys or payment processing.

## Included
- Martial arts, yoga and calisthenics lesson catalogue with blurred premium cards and fictional checkout.
- Fully offline, hard-coded course outlines with free in-app lesson detail pages; no YouTube integration.
- Recipe cards with estimated calories/macros and allergen filtering.
- Water and meal trackers, demo storefront/cart, fake offers, five pinned fictional community stories plus a five-minute random post generator, retaining the newest 20 generated posts.
- Light, dark and Darcula/AMOLED themes; promo code `Notiboi Piyush` unlocks premium lessons locally.

## Build APK
GitHub Actions builds a debug APK on pushes to main, pull requests and manual dispatch.
1. Open **Actions** and select **Build Android APK**.
2. Run the workflow or push a commit to main.
3. Download the `placeholder-fitness-debug-apk` artifact from the completed run.

The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Notes
Nutrition values are approximate per-serving estimates, not medical advice. Allergy filtering is a convenience only; independently verify labels and cross-contact. YouTube video search requires internet and remains subject to YouTube playback availability. All offers, prices, discounts, checkout results, posts and activity stats are fictional.

## Nutrition reference basis
The general food-group and balanced-meal approach is informed by the ICMR–National Institute of Nutrition's *Dietary Guidelines for Indians (2024)* and NHS general balanced-diet guidance. Recipe macro figures in this app are illustrative estimates created for the demo; they are not copied official nutrient analyses or individual calorie prescriptions.

- ICMR–NIN: https://www.nin.res.in/dietaryguidelines/pdfjs/locale/DGI24thJune2024fin.pdf
- NHS balanced diet: https://www.nhs.uk/Live-well/eat-well/how-to-eat-a-balanced-diet/eating-a-balanced-diet/

## Community feed behavior
The five original demo stories are pinned and never removed. While the app process is active, one fictional post is generated every five minutes using a random activity/pose and the two requested Piyush sentence templates. Generated posts receive random like counts; only the newest 20 generated posts are retained. No internet connection or server is used.
