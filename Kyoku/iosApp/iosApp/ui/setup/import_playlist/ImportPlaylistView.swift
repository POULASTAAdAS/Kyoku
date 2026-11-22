//
//  ImportPlaylistView.swift
//  iosApp
//
//  Created by Poulastaa Das on 22/11/26.
//

import ComposeApp
import SwiftUI

struct ImportPlaylistView: View {
    @Environment(Router<SetupRoute>.self) private var router

    @StateObject private var viewmodel = ImportPlaylistViewModel()

    var body: some View {
        Text( /*@START_MENU_TOKEN@*/"Hello, World!" /*@END_MENU_TOKEN@*/)
    }
}

#Preview {
    ImportPlaylistView()
        .environment(Router<SetupRoute>())
}
