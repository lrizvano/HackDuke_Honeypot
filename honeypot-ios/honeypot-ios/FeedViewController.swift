import UIKit

class FeedViewController: UIViewController {
    @IBOutlet var collectionView: UICollectionView!
    var messages = [Message]() {
        didSet {
            DispatchQueue.main.async {
                self.collectionView.reloadData()
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        HoneypotAPI.getMessages { result in
            switch result {
            case .success(let messages):
                self.messages = messages
            case .failure(let error):
                print(error)
            }
        }
    }
}

extension FeedViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return messages.count
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: 60)
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "feedCell", for: indexPath) as? FeedCollectionViewCell else {
            return UICollectionViewCell()
        }
        
        cell.lblMessage.text = messages[indexPath.item].msg
        cell.lblTime.text = messages[indexPath.item].time
        cell.lblId.text = messages[indexPath.item].id
        
        return cell
    }
}
