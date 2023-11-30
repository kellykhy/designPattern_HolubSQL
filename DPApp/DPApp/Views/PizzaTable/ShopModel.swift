//
//  PizzaModel.swift
//  DPApp
//
//  Created by 정의찬 on 11/29/23.
//

import Foundation

struct RestaurantResponse: Codable {
    var tableName: String
    var recodrs: [Restaurant]
}

struct Restaurant: Codable {
    var id: String
    var name: String
}

class RestaurantInfo {
    let restaurantInfo: [Restaurant] = [
    Restaurant(id: "0", name: "도미노 피자"),
    Restaurant(id: "1", name: "피자헛"),
    Restaurant(id: "2", name: "미스터 피자"),
    ]
}
