package com.ashish.filetax.privacy

data class PrivacySection(
    val title: String,
    val body: List<String>
)

object PrivacyPolicy {

    const val HEADLINE = "Nothing you type here leaves this phone"

    const val SUMMARY = "This app has no servers, no accounts and no analytics. It never asks for the internet, and it cannot send your answers, your documents or your figures anywhere, because there is nowhere for them to go."

    const val LAST_UPDATED = "Last updated 11 September 2026"

    val sections = listOf(
        PrivacySection(
            title = "What the app keeps",
            body = listOf(
                "Your answers to the questions, the documents you have ticked off, and the filing steps you have marked done. That is all of it.",
                "It is written to the ordinary per-app storage that the operating system gives every app, on this device only. No copy is made anywhere else."
            )
        ),
        PrivacySection(
            title = "What happens to a PDF you import",
            body = listOf(
                "The file is opened and read inside the app, on this phone. The text is pulled out on the device and matched against the figures the return asks for.",
                "The app never copies the PDF anywhere. The figures it reads stay in memory while the app is open, so your guide can show you which box each one goes into, and they are never written to storage.",
                "Tap Forget these documents, or Start over, and they are gone. Closing the app clears them too, and nothing about them survives to the next time you open it.",
                "If the PDF is locked, the password you type is used once to open it and is never written down."
            )
        ),
        PrivacySection(
            title = "What the app sends",
            body = listOf(
                "Nothing. There is no network code in the app: no upload, no download, no crash reporting, no analytics, no advertising, no third-party libraries that phone home.",
                "The app does not file your return and has no connection to the income tax department. When you are ready to file, you open incometax.gov.in yourself, and from that point the department's own site and its privacy policy apply."
            )
        ),
        PrivacySection(
            title = "What the app can reach on your phone",
            body = listOf(
                "Only the one file you pick, each time you pick it, through the system file picker. The app has no permission to browse your storage, and asks for no camera, contacts, location or phone permissions.",
                "There is no login, so the app never learns your name, email address or phone number unless you type one into a document field yourself."
            )
        ),
        PrivacySection(
            title = "Backups",
            body = listOf(
                "On Android the app opts out of system backup, so what it saves stays on the device.",
                "On iPhone the small amount it saves sits in the app's own storage, which your device's encrypted iCloud or computer backup may include if you have backups switched on."
            )
        ),
        PrivacySection(
            title = "Deleting everything",
            body = listOf(
                "Start over inside the app clears your answers, your ticked documents and your completed steps.",
                "Uninstalling the app, or clearing its data in system settings, removes the lot. Since nothing was ever sent anywhere, there is nothing to write in and ask anyone to delete."
            )
        ),
        PrivacySection(
            title = "Children and sensitive data",
            body = listOf(
                "Tax documents carry your PAN, your Aadhaar number, your bank account and your income. That is exactly why this app was built to work without a network: none of it is transmitted, and none of it is shared with anyone, ever.",
                "The app is meant for an adult filing their own return, or filing as someone's representative."
            )
        ),
        PrivacySection(
            title = "If this ever changes",
            body = listOf(
                "Any version that wanted to send something off the device would need a new permission and a rewrite of this page. Check this page after an update if that matters to you.",
                "This app gives guidance built from the published rules for the assessment year. It is not tax advice, and it is not a filing service."
            )
        )
    )
}