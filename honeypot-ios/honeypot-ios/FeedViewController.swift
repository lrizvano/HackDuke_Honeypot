import UIKit

class FeedViewController: UIViewController {
    @IBOutlet var collectionView: UICollectionView!
    
    enum ColorMode {
        case light
        case dark
    }
    
    private var mode: ColorMode {
        switch traitCollection.userInterfaceStyle {
        case .dark:
            return ColorMode.dark
        default:
            return ColorMode.light
        }
    }
    var messages = [Message]() {
        didSet {
            DispatchQueue.main.async {
                guard self.messages.count > 0 else { return }
                let indexPaths = Array(0...self.messages.count - 1).map { IndexPath(item: $0, section: 0) }
                self.collectionView.performBatchUpdates({
                    self.collectionView.insertItems(at: indexPaths)
                }, completion: nil)
            }
        }
    }

    override func viewDidLoad() {
        if traitCollection.userInterfaceStyle == .light {
            view.backgroundColor = UIColor(displayP3Red: 1, green: 248.0/255.0, blue: 210.0/255.0, alpha: 1.0)
            collectionView.backgroundColor = UIColor(displayP3Red: 1, green: 248.0/255.0, blue: 210.0/255.0, alpha: 1.0)
        }
         navigationController?.navigationBar.barTintColor = UIColor(displayP3Red: 170/255, green: 100/255, blue: 39/255, alpha: 1.0)
        
        super.viewDidLoad()
        
        let button = UIButton(type: .custom)
        button.setImage(#imageLiteral(resourceName: "favicon"), for: .normal)
        button.addTarget(self, action: #selector(leftBarButtonSelect(_:)), for: .touchDown)
        button.contentMode = .scaleAspectFit
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: self.navigationController!.navigationBar.frame.height - 20).isActive = true
        button.widthAnchor.constraint(equalToConstant: self.navigationController!.navigationBar.frame.height - 20).isActive = true
        let barButton = UIBarButtonItem(customView: button)
        self.navigationItem.rightBarButtonItem = barButton
        
        HoneypotAPI.getMessages { result in
            switch result {
            case .success(let messages):
                self.messages = messages
            case .failure(let error):
                print(error)
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.navigationController?.navigationBar.layer.masksToBounds = false
        self.navigationController?.navigationBar.layer.shadowColor = mode == .light ? UIColor.lightGray.cgColor : UIColor.black.cgColor
        self.navigationController?.navigationBar.layer.shadowOpacity = 0.8
        self.navigationController?.navigationBar.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        self.navigationController?.navigationBar.layer.shadowRadius = 2
    }
    
    @objc func leftBarButtonSelect(_ button: UIButton) {
        performSegue(withIdentifier: "toSettings", sender: self)
    }
}

extension FeedViewController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return messages.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "feedCell", for: indexPath) as? FeedCollectionViewCell else {
            return UICollectionViewCell()
        }
        
        cell.lblMessage.text = messages[indexPath.item].msg
        cell.lblTime.text = messages[indexPath.item].time
        cell.lblId.text = messages[indexPath.item].id
        cell.lblMessage.textColor = .black
        cell.lblTime.textColor = .gray
        cell.lblId.textColor = .lightGray

        cell.layer.masksToBounds = false
        cell.layer.shadowColor = mode == .light ? UIColor.lightGray.cgColor : UIColor.black.cgColor
        cell.layer.shadowOpacity = 0.8
        cell.layer.shadowOffset = CGSize(width: 0, height: 2.0)
        cell.layer.shadowRadius = 2
        
        cell.layer.cornerRadius = 14.0
        return cell
    }

    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        let alpha:CGFloat = mode == .light ? 1.0 : 0.75
        cell.backgroundColor = UIColor(displayP3Red: 246.0/255.0, green: 214.0/255.0, blue: 50.0/255.0, alpha: alpha)
    }
}
