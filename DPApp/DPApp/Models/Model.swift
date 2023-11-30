//
//  Model.swift
//  DPApp
//
//  Created by 정의찬 on 11/29/23.
//

import Foundation
import SwiftUI

struct MenuResponse: Codable {
    var tableName: String
    var records: [MenuItem]
}

struct MenuItem: Codable {
    var id: String
    var restaurantId: String
    var name: String
    var type: String
    var price: String

    enum CodingKeys: String, CodingKey {
        case id, name, type, price
        case restaurantId = "restaurant_id"
    }
}

struct RadioMenu {
    var id: Int
    var name: String
    var price: Int
    var isChecked : Bool //라디오값
    
    
    init(id: Int, isChecked: Bool = false, name: String, price: Int) {
        self.id = id
        self.isChecked = isChecked
        self.name = name
        self.price = price
        
    }
    static let dummyData: [RadioMenu] = [
        RadioMenu(id: 1, isChecked: true, name: "포테이토 L", price: 6500),
        RadioMenu(id: 2, isChecked: false, name: "블랙타이거 슈림프L", price: 12000),
        RadioMenu(id: 3, isChecked: false, name: "와일드 웨스트 스테이크L", price: 17500)
    ]
}
struct Menu {
    var id: Int
    var name: String
    var price: Int
    var isChecked : Bool
    
    
    init(id: Int, isChecked: Bool = false, name: String, price: Int) {
        self.id = id
        self.isChecked = isChecked
        self.name = name
        self.price = price
        
    }
    static let dummyData: [Menu] = [
        Menu(id: 1, isChecked: false, name: "오리지널 도우", price: 1000),
        Menu(id: 2, isChecked: false, name: "나폴리 도우", price: 2000),
        Menu(id: 3, isChecked: false, name: "씬 도우", price: 2000),
        Menu(id: 4, isChecked: false, name: "오리지널 슈퍼시드 화이버 함유 도우", price: 1000),
        Menu(id: 5, isChecked: false, name: "나폴리 도우 트리플 치즈버스트 엣지", price: 2000),

        Menu(id: 12, isChecked: false, name: "맥앤치즈 볼", price: 8000),
        Menu(id: 13, isChecked: false, name: "불고기 대파 크림치즈 파스타", price: 8900),
        Menu(id: 14, isChecked: false, name: "체다 치즈 치킨", price: 69000),
        Menu(id: 15, isChecked: false, name: "웨스턴 핫 윙", price: 69000),
        Menu(id: 16, isChecked: false, name: "베이컨 까르보나라 페투치니", price: 7000),
        Menu(id: 17, isChecked: false, name: "치킨 텐더 6조각", price: 15000),
    ]
}

struct RadioButtonView: View {
    var title: String
    var price: Int
    @Binding var isChecked: Bool
    
    var body: some View {
        HStack {
            RadioButton(isChecked: $isChecked)
            Text(title)
            Spacer()
            Text("+\(price)원")
                .font(Font.system(size: 17))
                .fontWeight(.bold)
        
        }
    }
}

struct CheckBoxView: View {
    var title: String
    var price: Int
    @Binding var isChecked: Bool
    
    var body: some View {
        HStack {
            CheckBox(isChecked: $isChecked)
            Text(title)
            Spacer()
            Text("+\(price)원")
                .font(Font.system(size: 17))
                .fontWeight(.bold)
        }
    }
}

struct RadioButton: View {
    @Binding var isChecked: Bool
    var body: some View {
        Image(systemName: isChecked ? "dot.circle.fill" : "circle")
            .onTapGesture {
                isChecked.toggle()
            }
    }
}

struct CheckBox: View {
    @Binding var isChecked: Bool
    
    var body: some View {
        Image(systemName: isChecked ? "checkmark.square" : "square")
            .onTapGesture {
                isChecked.toggle()
            }
    }
}
