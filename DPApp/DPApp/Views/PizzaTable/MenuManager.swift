//
//  MenuManager.swift
//  DPApp
//
//  Created by 정의찬 on 11/30/23.
//

import Foundation

class MenuManager {
    static let shared = MenuManager()
    private init() {}

    var menuItems: [MenuItem] = []

    // 특정 타입의 메뉴 아이템을 필터링하는 함수
    func items(ofType type: String) -> [MenuItem] {
        return menuItems.filter { $0.type == type }
    }
}
