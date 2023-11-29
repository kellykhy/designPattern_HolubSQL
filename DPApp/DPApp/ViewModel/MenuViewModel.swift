//
//  MenuViewModel.swift
//  DPApp
//
//  Created by 정의찬 on 11/29/23.
//

import Foundation

final class OrderViewModel: ObservableObject {
    @Published var radioMenus: [RadioMenu] = []
    @Published var menus: [Menu] = []
    
    @Published var selectedMenusHistory: [[Menu]]
    @Published var selectedRadioMenusHistory: [[RadioMenu]]
    
    @Published var totalPrice: Int = 0
    @Published var allTotalCount: Int = 0
    @Published var allTotalPrice: Int = 0
    
    @Published var totalPriceHistory: [Int] = []
    @Published var totalCountHistory: [Int] = []
    
    @Published var count: Int = 1
    
    init(){
        selectedMenusHistory = []
        selectedRadioMenusHistory = []
        totalPriceHistory = []
        totalCountHistory = []
        updateTotals()
        
        //더미
        radioMenus = RadioMenu.dummyData
        menus = Menu.dummyData
        
        totalPrice =  radioMenus.filter { $0.isChecked }
            .map { $0.price }.reduce(0, +)
    }
    
    var selectedMenus: [Menu] {
        menus.filter { $0.isChecked }
    }
    
    var selectedRadioMenus: [RadioMenu] {
        radioMenus.filter { $0.isChecked }
    }
    
    func updateRadoiMenuSelection(selectedId: Int){
        for i in radioMenus.indices {
            radioMenus[i].isChecked = (radioMenus[i].id == selectedId)
        }
        updateTotals()
    }
    
    func changeCount(_ change: Int) {
        count = max(1,count + change)
        updateTotals()
    }
    
    func updateTotals() {
        let selectedPrices = selectedMenus.map { $0.price }
            let radioPrices = selectedRadioMenus.map { $0.price }
            
            let selectedTotalPrice = selectedPrices.reduce(0, +)
            let radioTotalPrice = radioPrices.reduce(0, +)
            
            totalPrice = (selectedTotalPrice + radioTotalPrice) * count
    }
    
    func resetAndCalculateAllTotals() {
        
            allTotalCount += count
            allTotalPrice += totalPrice
            
            // 선택된 메뉴들의 기록을 저장
            selectedRadioMenusHistory.append(selectedRadioMenus)
            selectedMenusHistory.append(selectedMenus)
            totalPriceHistory.append(totalPrice)
            totalCountHistory.append(count)
            
           
        for i in radioMenus.indices {
            if i == 0 {
                radioMenus[i].isChecked = true
            }else {
                radioMenus[i].isChecked = false
            }
        }
            // 메뉴들을 초기화
            for i in menus.indices {
                menus[i].isChecked = false
            }
            for i in menus.indices {
                menus[i].isChecked = false
            }
        totalPrice = radioMenus.filter{$0.isChecked}
            .first?.price ?? 1000
               
            count = 1
        
    }
}
