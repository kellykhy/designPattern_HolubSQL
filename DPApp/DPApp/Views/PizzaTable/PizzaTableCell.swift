//
//  pizzaTableCell.swift
//  DPApp
//
//  Created by 정의찬 on 11/29/23.
//

import Foundation
import UIKit
import SnapKit

class PizzaTableCell: UITableViewCell {
    static let identifier = "pizzaTableCell"
    
    private lazy var shopName: UILabel = {
        let label = UILabel()
        label.textColor = UIColor.black
        label.font = UIFont.systemFont(ofSize: 30)
        return label
    }()
    
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        makeConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("error")
    }
    
    private func makeConstraints() {
        self.addSubview(shopName)
        
        shopName.snp.makeConstraints{ make in
            make.centerX.centerY.equalToSuperview()
        }
    }
    
    final func configure(_ text: String){
        self.shopName.text = text
    }
    
    final func modelConfigure(_ model: Restaurant){
        self.shopName.text = model.name
    }
    
}
