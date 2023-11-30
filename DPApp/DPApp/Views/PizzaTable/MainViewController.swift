//
//  UIViewController.swift
//  DPApp
//
//  Created by 정의찬 on 11/29/23.
//

import Foundation
import UIKit
import SwiftUI
import SnapKit
import Alamofire

class MainViewController: UIViewController {
    
    private var restaurants: [Restaurant] = []
    private var selectedRestaurantId: String?
    
    private lazy var titleText: UILabel = {
        let label = UILabel()
        label.text = "피자 가게를 고르세요!!"
        label.font = UIFont.systemFont(ofSize: 30)
        label.textColor = UIColor.black
        return label
    }()
    
    private lazy var pizzatable: UITableView = {
        let table = UITableView()
        table.register(PizzaTableCell.self, forCellReuseIdentifier: PizzaTableCell.identifier)
        table.delegate = self
        table.dataSource =  self
        return table
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        makeConstraints()
        fetchRestaurant()
    }
    
    private func makeConstraints(){
        self.view.addSubview(titleText)
        self.view.addSubview(pizzatable)
        
        
        titleText.snp.makeConstraints{ make in
            make.top.left.right.equalToSuperview()
        }
        
        pizzatable.snp.makeConstraints{ make in
            make.top.equalTo(titleText.snp.bottom).offset(30)
            make.bottom.left.right.equalToSuperview()
            
        }
    }
    
    private func fetchMenu(){
        guard let restaurantId = selectedRestaurantId else { return }
        let url = "http://localhost:8080/restaurants?restaurant_id=\(restaurantId)"
        AF.request(url).responseDecodable(of: MenuResponse.self) { response in
            switch response.result {
            case.success(let menuResponse):
                MenuManager.shared.menuItems = menuResponse.records
            case.failure(let error):
                print(error)
            }
        }
    }
    
    private func fetchRestaurant(){
        AF.request("http://localhost:8080/restaurants").responseDecodable(of: RestaurantResponse.self) { response in
            switch response.result {
            case.success(let data):
                self.restaurants = data.records
                DispatchQueue.main.async {
                    self.pizzatable.reloadData()
                }
            case.failure(let error):
                print(error)
            }
        }
    }
}

extension MainViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return restaurants.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: PizzaTableCell.identifier, for: indexPath) as? PizzaTableCell else { return UITableViewCell() }
        
        let restaurant = restaurants[indexPath.row]
        cell.configure(restaurant.name)
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 120
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
 
        
        let swiftUIView = ContentView(viewModel: OrderViewModel())
        let hostingController = UIHostingController(rootView: swiftUIView)
        self.present(hostingController, animated: true, completion: nil)

    }
    
}

struct MainViewControllerRepresentable: UIViewControllerRepresentable{
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
    }
    
    func makeUIViewController(context: Context) -> some UIViewController {
        MainViewController()
    }
}
