import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init(){
        NapierProxyKt.debugBuild()
        KoinHelperKt.doInitKoin()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
