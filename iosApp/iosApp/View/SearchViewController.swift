//
//  SearchViewController.swift
//  iosApp
//
//  Created by Ali on 7/5/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit
import GooglePlaces
import ComposeApp

class IOSSearchNativeViewFactory: NativeSearchViewFactory {
    func createSearchView(onPlaceSelected: @escaping (KotlinDouble, KotlinDouble) -> Void, onDone: @escaping () -> Void) -> UIViewController {
        return SearchViewController(onPlaceSelected: onPlaceSelected, onDone: onDone)
    }
    
    static var shared = IOSSearchNativeViewFactory()
}

class SearchViewController: UIViewController, GMSAutocompleteViewControllerDelegate {
    private let onPlaceSelected: (KotlinDouble, KotlinDouble) -> Void
    private let onDone: () -> Void
    
    init(onPlaceSelected: @escaping (KotlinDouble, KotlinDouble) -> Void,
         onDone: @escaping () -> Void) {
        self.onPlaceSelected = onPlaceSelected
        self.onDone = onDone
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        showAutocompleteController()
    }
    
    private func showAutocompleteController() {
        let autocompleteController = GMSAutocompleteViewController()
        autocompleteController.delegate = self
        present(autocompleteController, animated: true, completion: nil)
    }
    
    func viewController(_ viewController: GMSAutocompleteViewController, didAutocompleteWith place: GMSPlace) {
        print("✅ Selected: \(place.name ?? "unknown")")
        dismiss(animated: true) {
            self.onPlaceSelected(KotlinDouble(value: place.coordinate.latitude),
                                 KotlinDouble(value: place.coordinate.longitude))
        }
    }
    
    func viewController(_ viewController: GMSAutocompleteViewController, didFailAutocompleteWithError error: Error) {
        print("❌ Autocomplete error: \(error.localizedDescription)")
        dismiss(animated: true) {
            self.onDone()
        }
    }
    
    func wasCancelled(_ viewController: GMSAutocompleteViewController) {
        print("❌ Autocomplete cancelled")
        dismiss(animated: true) {
            self.onDone()
        }
    }
}
