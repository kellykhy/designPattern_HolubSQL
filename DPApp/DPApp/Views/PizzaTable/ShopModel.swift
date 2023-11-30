//
//  PizzaModel.swift
//  DPApp
//
//  Created by 정의찬 on 11/29/23.
//

import Foundation

struct RestaurantResponse: Codable {
    var tableName: String
    var records: [Restaurant]
}

struct Restaurant: Codable {
    var id: String
    var name: String
}
