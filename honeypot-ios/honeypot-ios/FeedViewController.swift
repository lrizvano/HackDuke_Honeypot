import UIKit

class FeedViewController: UIViewController {
    @IBOutlet var tableView: UITableView!
    var messages = [Message]() {
        didSet {
            DispatchQueue.main.async {
                self.tableView.reloadData()
            }
        }
    }

    override func viewDidLoad() {
        if traitCollection.userInterfaceStyle == .light {
            view.backgroundColor = UIColor(displayP3Red: 1, green: 248.0/255.0, blue: 210.0/255.0, alpha: 1.0)
            tableView.backgroundColor = UIColor(displayP3Red: 1, green: 248.0/255.0, blue: 210.0/255.0, alpha: 1.0)
        }
        
        super.viewDidLoad()
        let imageView = UIImageView(image: #imageLiteral(resourceName: "header"))
        imageView.contentMode = .scaleAspectFit
        self.navigationItem.titleView = imageView
        
        tableView.rowHeight = UITableView.automaticDimension
        tableView.estimatedRowHeight = 60


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

extension FeedViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "feedCell", for: indexPath) as? FeedTableViewCell else {
            return UITableViewCell()
        }
        
        cell.lblMessage.text = messages[indexPath.item].msg
        cell.lblTime.text = messages[indexPath.item].time
        cell.lblId.text = messages[indexPath.item].id
        if traitCollection.userInterfaceStyle == .light {
            cell.lblMessage.textColor = .black
            cell.lblTime.textColor = .gray
            cell.lblId.textColor = .lightGray
        } else {
            cell.lblMessage.textColor = .black
            cell.lblTime.textColor = .gray
            cell.lblId.textColor = .lightGray
        }
        cell.layer.cornerRadius = 14.0
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if traitCollection.userInterfaceStyle == .light {
            cell.backgroundColor = UIColor(displayP3Red: 246.0/255.0, green: 214.0/255.0, blue: 50.0/255.0, alpha: 1.0)
        } else {
            cell.backgroundColor = UIColor(displayP3Red: 246.0/255.0, green: 214.0/255.0, blue: 50.0/255.0, alpha: 0.75)
        }
    }
}
