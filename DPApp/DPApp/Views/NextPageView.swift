//
//  NextPageView.swift
//  DPApp
//
//  Created by 정의찬 on 11/29/23.
//

import SwiftUI

struct NextPageView: View {
    @ObservedObject var viewModel: OrderViewModel
    
    var body: some View {
        GeometryReader { geometry in

        VStack{
            List {
                Section(header: Text("").font(.headline)) {
                    ForEach(viewModel.selectedMenusHistory.indices, id: \.self) { index in
                        NextCartView(
                            selectedRadioMenu: viewModel.selectedRadioMenusHistory[index],
                            selectedMenus: viewModel.selectedMenusHistory[index],
                            totalPrice: viewModel.totalPriceHistory[index],
                            count: viewModel.totalCountHistory[index]
                        )
                    }
                }
                
                Section(header: Text("")) {
                    Text("배달로 받을게요")
                        .font(.headline)
                }
                Section(header: Text("")) {
                    VStack{
                        Spacer()
                        HStack {
                            
                            Text("총 주문금액")
                            Spacer()
                            Text("\(viewModel.allTotalPrice)원")
                        }
                        Spacer()
                        
                        HStack {
                            Text("배달팁")
                            Spacer()
                            Text("2,000원")
                        }
                        Spacer()
                        Divider()
                        HStack {
                            Text("결제예정금액")
                            
                            Spacer()
                            Text("\(viewModel.allTotalPrice + 2000)원")
                        }
                        .padding(.bottom,10)
                        
                    }
                }
            }
            .listStyle(GroupedListStyle())
            .navigationBarTitle("장바구니")
            
            ZStack{
                RoundedRectangle(cornerRadius: 5)
                    .fill(Color("LogoColor"))
                    .frame(width: geometry.size.width * 0.85, height: 50)
                
                HStack{
                    Text("\(viewModel.totalPrice)원 담기")
                        .padding(EdgeInsets(top: 5, leading: 5, bottom: 5, trailing: 5))
                        .background(Color("LogoColor"))
                        .foregroundColor(.white)
                        .fontWeight(.bold)
                        .cornerRadius(5)
                    
                    Text("・ 알뜰배달 주문하기")
                        .padding(EdgeInsets(top: 5, leading: 5, bottom: 5, trailing: 5))
                        .background(Color("LogoColor"))
                        .foregroundColor(.white)
                        .fontWeight(.bold)
                        .cornerRadius(5)
                }
            }
            
        }
        }
    }
}
